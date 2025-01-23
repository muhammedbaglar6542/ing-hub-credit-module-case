package com.inghub.creditmodule.controller;

import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.request.CustomerRequest;
import com.inghub.creditmodule.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerRequest customerRequest) {
        log.info("Creating customer: {}", customerRequest);
        customerService.createCustomer(customerRequest);
        return ResponseEntity.ok("Customer creaeted successfully");
    }


    @GetMapping("/find/{customerId}")
    public ResponseEntity<Customer> findCustomer(@PathVariable Long customerId) throws Exception {
        log.info("Fetching  customer: {}", customerId);
        return ResponseEntity.ok(customerService.findCustomer(customerId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Customer>> findAllCustomer() throws Exception {
        log.info("Fetching  all customers");
        return ResponseEntity.ok(customerService.findAllCustomer());
    }


}
