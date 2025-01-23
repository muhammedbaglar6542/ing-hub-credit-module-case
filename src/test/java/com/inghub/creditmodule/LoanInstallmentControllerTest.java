package com.inghub.creditmodule;

import com.inghub.creditmodule.controller.LoanInstallmentController;
import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.service.LoanInstallmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoanInstallmentControllerTest {

    @Mock
    private LoanInstallmentService loanInstallmentService;

    @InjectMocks
    private LoanInstallmentController loanInstallmentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanInstallmentController).build();
    }

    @Test
    void testFindByLoanInstallmentId() throws Exception {
        Long loanInstallmentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/loanInstallment/findByInstallment/{loanInstallmentId}", loanInstallmentId)).andExpect(status().isOk());
        verify(loanInstallmentService, times(1)).findByLoanInstallmentId(loanInstallmentId);

    }

    @Test
    void testFindByLoanId() throws Exception {
        Long loanId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/loanInstallment/findByLoan/{loanId}", loanId));

        verify(loanInstallmentService, times(1)).findByLoanId(loanId);
    }

    @Test
    void testFindAllLoanInstallments() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/loanInstallment/findAll")).andExpect(status().isOk()) ;

        verify(loanInstallmentService, times(1)).findAllLoanInstallments();
    }
}