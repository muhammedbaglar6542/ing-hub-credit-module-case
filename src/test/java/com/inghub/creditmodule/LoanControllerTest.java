package com.inghub.creditmodule;

import com.inghub.creditmodule.controller.LoanController;
import com.inghub.creditmodule.model.Loan;
import com.inghub.creditmodule.request.LoanRequest;
import com.inghub.creditmodule.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    public void testCreateLoan() throws Exception {
        loanService.createLoan(any(LoanRequest.class));

        mockMvc.perform(post("/api/loan/create")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan created successfully"));

        verify(loanService, times(1)).createLoan(any(LoanRequest.class));
    }

    @Test
    public void testFindByLoanId() throws Exception {
        Long loanId = 1L;
        Loan loan = new Loan();
        when(loanService.findByLoanId(loanId)).thenReturn(loan);

        mockMvc.perform(get("/api/loan/find/" + loanId))
                .andExpect(status().isOk());

        verify(loanService, times(1)).findByLoanId(loanId);
    }

    @Test
    public void testPayLoan() throws Exception {
        Long loanId = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(100.00);
        loanService.payLoan(loanId, paymentAmount);

        mockMvc.perform(post("/api/loan/pay/{loanId}/{paymentAmount}", loanId, paymentAmount))
                .andExpect(status().isOk());

    }


    @Test
    public void testFindAllLoan() throws Exception {
        List<Loan> loans = Arrays.asList(new Loan(), new Loan());
        when(loanService.findAllLoan()).thenReturn(loans);

        mockMvc.perform(get("/api/loan/findAll"))
                .andExpect(status().isOk());

        verify(loanService, times(1)).findAllLoan();
    }
}