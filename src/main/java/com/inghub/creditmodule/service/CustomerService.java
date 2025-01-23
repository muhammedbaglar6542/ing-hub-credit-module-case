package com.inghub.creditmodule.service;

import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.request.CustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public void createCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder().name(customerRequest.getName()).surname(customerRequest.getSurname()).creditLimit(customerRequest.getCreditLimit()).usedCreditLimit(customerRequest.getUsedCreditLimit()).build();
        customerRepository.save(customer);
    }

    public Customer findCustomer(Long customerId) throws Exception {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if (customer.isEmpty()) {
            throw new Exception("Customer not found!");
        }

        return customer.get();

    }

    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }
}
