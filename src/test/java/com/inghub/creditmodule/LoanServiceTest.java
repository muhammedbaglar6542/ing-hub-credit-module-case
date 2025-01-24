package com.inghub.creditmodule;

import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.model.Loan;
import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.repository.LoanRepository;
import com.inghub.creditmodule.request.LoanRequest;
import com.inghub.creditmodule.service.CustomerService;
import com.inghub.creditmodule.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Mock
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoanService loanService;

    private LoanRequest loanRequest;
    private Customer customer;
    private Loan loan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder().name("customer").password("customer").creditLimit(BigDecimal.valueOf(1000000)).usedCreditLimit(BigDecimal.valueOf(1000)).build();
        customerRepository.save(customer);

        loan = Loan.builder().customer(customer).customerId(customer.getCustomerId()).loanAmount(BigDecimal.valueOf(150000)).numberOfInstallment(6).createDate(LocalDate.of(2025, 2, 12)).build();

        loanRepository.save(loan);

        List<LoanInstallment> loanInstallmentList = new ArrayList<>();
        LoanInstallment loanInstallment;

        for (int i = 0; i < loan.getNumberOfInstallment(); i++) {
            loanInstallment = LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2025, 3 + i, 1)).build();
            loanInstallmentList.add(loanInstallment);
        }

        loanInstallmentRepository.saveAll(loanInstallmentList);
        loan.setLoanInstallments(loanInstallmentList);

        loanRequest=LoanRequest.builder().
                customerId(loan.getCustomerId()).
                loanAmount(BigDecimal.valueOf(10000)).
                numberOfInstallment(12).
                createDate(LocalDate.now()).
                interestRate(BigDecimal.valueOf(0.3)).
                build();

    }

    @Test
    void testCreateLoan()  {
        when(customerService.findCustomer(loanRequest.getCustomerId())).thenReturn(customer);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        loanService.createLoan(loanRequest);

        verify(customerService, times(1)).findCustomer(loanRequest.getCustomerId());
        verify(loanRepository, times(2)).save(any(Loan.class));
        assertNotNull(loan.getLoanInstallments());
        assertEquals(6, loan.getLoanInstallments().size());
    }

    @Test
    void testCreateLoanWithInsufficientCreditLimit()  {
        loanRequest.setLoanAmount(BigDecimal.valueOf(60000000));

        when(customerService.findCustomer(loanRequest.getCustomerId())).thenReturn(customer);

        Exception exception = assertThrows(Exception.class, () -> {
            loanService.createLoan(loanRequest);
        });

        assertEquals("Limit is not enough!", exception.getMessage());
    }

    @Test
    void testPayLoan()  {
        when(loanRepository.findByLoanId(1L)).thenReturn(Optional.of(loan));
        BigDecimal paymentAmount = BigDecimal.valueOf(250000);

        loanService.payLoan(1L, paymentAmount);

        verify(loanInstallmentRepository, times(3)).save(any(LoanInstallment.class));
        assertTrue(loan.getLoanInstallments().get(0).isPaid());
    }

    @Test
    void testPayLoanWithInsufficientAmount()  {
        when(loanRepository.findByLoanId(1L)).thenReturn(Optional.of(loan));
        BigDecimal paymentAmount = BigDecimal.valueOf(100);

        Exception exception = assertThrows(Exception.class, () -> {
            loanService.payLoan(1L, paymentAmount);
        });

        assertEquals("The payment amount entered is not sufficient!", exception.getMessage());
    }

    @Test
    void testFindByLoanId()  {
        when(loanRepository.findByLoanId(1L)).thenReturn(Optional.of(loan));

        Loan result = loanService.findByLoanId(1L);

        assertNotNull(result);
        assertEquals(loan, result);
    }

    @Test
    void testFindByLoanIdNotFound() {
        when(loanRepository.findByLoanId(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            loanService.findByLoanId(999L);
        });

        assertEquals("Loan not found!", exception.getMessage());
    }
}
