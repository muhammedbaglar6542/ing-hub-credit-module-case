package com.inghub.creditmodule;

import com.inghub.creditmodule.controller.CustomerController;
import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.request.CustomerRequest;
import com.inghub.creditmodule.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest();
        customerService.createCustomer(customerRequest);

        mockMvc.perform(post("/api/customer/create").contentType("application/json").content("{}"))
                .andExpect(status().isOk()).andExpect(content().string("Customer creaeted successfully"));

        verify(customerService, times(1)).createCustomer(customerRequest);
    }

    @Test
    public void testFindCustomer() throws Exception {
        Long customerId = 1L;
        Customer customer = new Customer();
        when(customerService.findCustomer(customerId)).thenReturn(customer);

        mockMvc.perform(get("/api/customer/find/" + customerId)).andExpect(status().isOk());
        verify(customerService, times(1)).findCustomer(customerId);
    }

    @Test
    public void testFindAllCustomer() throws Exception {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer());
        when(customerService.findAllCustomer()).thenReturn(customers);

        mockMvc.perform(get("/api/customer/findAll")).andExpect(status().isOk());

        verify(customerService, times(1)).findAllCustomer();
    }

}