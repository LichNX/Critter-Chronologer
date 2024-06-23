package com.udacity.jdnd.course3.critter.service;


import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public PetDTO savePet(PetDTO petDTO) {
        Optional<Customer> customerOpt = customerRepository.findById(petDTO.getOwnerId());

        if(customerOpt.isPresent()){
            Pet pet = revertPetDTOToPet(petDTO, customerOpt.get());
            Pet petSave = petRepository.save(pet);

            Customer customer = pet.getCustomer();
            List<Pet> customerPets = customer.getPets();
            if(customerPets == null){
                customerPets = new ArrayList<>();
            }
            customerPets.add(pet);
            customer.setPets(customerPets);
            customerRepository.save(customer);

            return convertPetToPetDTO(petSave);
        }
        return null;
    }

    @Override
    public PetDTO getPet(long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if(pet.isPresent()) {
            return convertPetToPetDTO(pet.get());
        }
        return null;
    }

    @Override
    public List<PetDTO> getPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOS = pets.stream()
                                .map(pet -> convertPetToPetDTO(pet))
                                .collect(Collectors.toList());
        return petDTOS;
    }

    @Override
    public List<PetDTO> getPetsByOwner(long ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);
        List<PetDTO> petDTOS = pets.stream()
                .map(pet -> convertPetToPetDTO(pet))
                .collect(Collectors.toList());
        return petDTOS;
    }

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    private Pet revertPetDTOToPet(PetDTO petDTO, Customer customer){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet, "customer");
        pet.setCustomer(customer);
        return pet;
    }
}
