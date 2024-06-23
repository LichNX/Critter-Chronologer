package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;


    public Customer getCustomerById(long customerId) {
        return customerRepository.findById(customerId).get();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if(customerDTO.getPetIds() == null) {
            Customer customer = revertCustomerDTOToCustomer(customerDTO, null);
            Customer customerSave = customerRepository.save(customer);
            return converCustomerToCustomerDTO(customerSave);
        }
        List<Pet> pets = petRepository.findAllById(customerDTO.getPetIds());
        Customer customer = revertCustomerDTOToCustomer(customerDTO, pets);
        Customer customerSave = customerRepository.save(customer);
        return converCustomerToCustomerDTO(customerSave);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if(customers != null) {
            return customers.stream()
                    .map(customer -> converCustomerToCustomerDTO(customer))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public CustomerDTO getOwnerByPet(long petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        if(pet.isPresent()) {
            long customerId = pet.get().getCustomer().getId();
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            return converCustomerToCustomerDTO(customerOptional.get());
        }
        return null;
    }

    private CustomerDTO converCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO, "pets");
        if(customer.getPets() != null) {
            List<Long> petIds = customer.getPets().stream()
                                                    .map(pet -> pet.getId())
                                                    .collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Customer revertCustomerDTOToCustomer(CustomerDTO customerDTO, List<Pet> pets) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer, "petIds");
        customer.setPets(pets);
        return customer;
    }
}
