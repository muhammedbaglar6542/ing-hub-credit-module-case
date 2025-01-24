package com.inghub.creditmodule;

import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.request.CustomerRequest;
import com.inghub.creditmodule.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("muhammed");
        customerRequest.setSurname("baglar");
        customerRequest.setCreditLimit(BigDecimal.valueOf(1000));
        customerRequest.setUsedCreditLimit(BigDecimal.ZERO);
        Customer customer = Customer.builder().name("muhammed").surname("baglar").creditLimit(BigDecimal.valueOf(1000)).usedCreditLimit(BigDecimal.ZERO).build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.createCustomer(customerRequest);

        verify(customerRepository, times(1)).save(argThat(c ->
                "muhammed".equals(c.getName()) &&
                        "baglar".equals(c.getSurname()) &&
                        BigDecimal.valueOf(1000).equals(c.getCreditLimit()) &&
                        BigDecimal.ZERO.equals(c.getUsedCreditLimit())
        ));
    }

    @Test
    void testFindCustomer_ExistingCustomer()  {
        Long customerId = 1L;
        Customer customer = Customer.builder().customerId(customerId).build();
        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findCustomer(customerId);

        assertEquals(customerId, foundCustomer.getCustomerId());
        verify(customerRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void testFindCustomer_CustomerNotFound() {
        Long customerId = 1L;
        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> customerService.findCustomer(customerId), "Customer not found!");
        verify(customerRepository, times(1)).findByCustomerId(customerId);
    }

}