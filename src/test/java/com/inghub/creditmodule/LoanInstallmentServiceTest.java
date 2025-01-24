package com.inghub.creditmodule;


import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.service.LoanInstallmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanInstallmentServiceTest {

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @InjectMocks
    private LoanInstallmentService loanInstallmentService;

    private LoanInstallment installment1;
    private LoanInstallment installment2;
    private List<LoanInstallment> installmentList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        installment1 = new LoanInstallment();
        installment2 = new LoanInstallment();

        installmentList = Arrays.asList(installment1, installment2);
    }

    @Test
    void testFindByLoanId() {
        Long loanId = 100L;
        when(loanInstallmentRepository.findByLoanId(loanId)).thenReturn(Optional.of(installmentList));

        Optional<List<LoanInstallment>> result = loanInstallmentService.findByLoanId(loanId);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(loanInstallmentRepository, times(1)).findByLoanId(loanId);
    }

    @Test
    void testFindByLoanInstallmentId() {
        Long installmentId = 1L;
        when(loanInstallmentRepository.findByInstallmentId(installmentId)).thenReturn(Optional.of(installment1));

        Optional<LoanInstallment> result = loanInstallmentService.findByLoanInstallmentId(installmentId);

        assertTrue(result.isPresent());
        assertEquals(installment1, result.get());
        verify(loanInstallmentRepository, times(1)).findByInstallmentId(installmentId);
    }

    @Test
    void testFindByLoanInstallmentIdNotFound() {
        Long installmentId = 999L;
        when(loanInstallmentRepository.findByInstallmentId(installmentId)).thenReturn(Optional.empty());

        Optional<LoanInstallment> result = loanInstallmentService.findByLoanInstallmentId(installmentId);

        assertFalse(result.isPresent());
        verify(loanInstallmentRepository, times(1)).findByInstallmentId(installmentId);
    }


}
