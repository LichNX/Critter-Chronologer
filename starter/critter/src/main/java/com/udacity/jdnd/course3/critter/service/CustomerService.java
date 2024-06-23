package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CustomerService {
    Customer getCustomerById(long customerId);

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getOwnerByPet(long petId);
}
