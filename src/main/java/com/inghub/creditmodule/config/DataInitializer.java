package com.inghub.creditmodule.config;

import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.model.Loan;
import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.repository.CustomerRepository;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanInstallmentRepository loanInstallmentRepository;

    @Autowired
    private LoanRepository loanRepository;


    @Override
    @Transactional
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            log.info("Initializing test data...");
            createCustomer1();
            createCustomer2();


            log.info("Test data initialized");
        }
    }

    private void createCustomer2() {

        Customer customer = Customer.builder().name("ing").password("ing").build();
        customerRepository.save(customer);

        Loan loan = Loan.builder().customer(customer).customerId(customer.getCustomerId()).loanAmount(BigDecimal.valueOf(150000)).numberOfInstallment(6).createDate(LocalDate.of(2025, 2, 12)).build();

        loanRepository.save(loan);

        List<LoanInstallment> loanInstallmentList = new ArrayList<>();
        loanInstallmentList.add(LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2024, 10, 1)).paid(true).build());
        loanInstallmentList.add(LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2024, 11, 1)).paid(true).build());
        loanInstallmentList.add(LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2024, 12, 1)).paid(true).build());
        loanInstallmentList.add(LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2025, 1, 1)).paid(true).build());
        loanInstallmentList.add(LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2025, 2, 1)).paid(true).build());
        loanInstallmentList.add(LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2025, 3 , 1)).build());

        loanInstallmentRepository.saveAll(loanInstallmentList);



    }

    private void createCustomer1() {

        Customer customer = Customer.builder().name("customer").password("customer").build();
        customerRepository.save(customer);

        Loan loan = Loan.builder().customer(customer).customerId(customer.getCustomerId()).loanAmount(BigDecimal.valueOf(150000)).numberOfInstallment(6).createDate(LocalDate.of(2025, 2, 12)).build();

        loanRepository.save(loan);

        List<LoanInstallment> loanInstallmentList = new ArrayList<>();
        LoanInstallment loanInstallment;

        for (int i = 0; i < loan.getNumberOfInstallment(); i++) {
            loanInstallment = LoanInstallment.builder().loan(loan).loanId(loan.getLoanId()).amount(BigDecimal.valueOf(25000)).paidAmount(BigDecimal.valueOf(25000)).dueDate(LocalDate.of(2025, 3 + i, 1)).build();
            loanInstallmentList.add(loanInstallment);
        }

        loanInstallmentRepository.saveAll(loanInstallmentList);



    }
}
