package com.inghub.creditmodule.service;

import com.inghub.creditmodule.model.Customer;
import com.inghub.creditmodule.model.Loan;
import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import com.inghub.creditmodule.repository.LoanRepository;
import com.inghub.creditmodule.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final List<Integer> NUMBER_OF_INSTALLMENTS = List.of(6, 9, 12, 24);
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final CustomerService customerService;

    public void createLoan(LoanRequest loanRequest) throws Exception {

        Customer customer = customerService.findCustomer(loanRequest.getCustomerId());

        BigDecimal availableLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());

        checkLoanCondition(loanRequest, availableLimit);

        BigDecimal totalAmount = loanRequest.getLoanAmount().multiply(BigDecimal.ONE.add(loanRequest.getInterestRate()));

        BigDecimal averageInstallmentAmount = totalAmount.divide(new BigDecimal(loanRequest.getNumberOfInstallment()), 2, RoundingMode.UP);

        List<LoanInstallment> installments = createInstallments(averageInstallmentAmount, loanRequest.getNumberOfInstallment());

        createLoan(customer, installments, loanRequest, totalAmount);

    }

    @Transactional
    private void createLoan(Customer customer, List<LoanInstallment> installments, LoanRequest loanRequest, BigDecimal totalAmount) {

        Loan loan = new Loan();
        loan.setLoanAmount(totalAmount);
        loan.setCreateDate(LocalDate.now());
        loan.setPaid(false);
        loan.setNumberOfInstallment(loanRequest.getNumberOfInstallment());
        loan.setCustomer(customer);
        loan.setLoanInstallments(installments);

        loanRepository.save(loan);
    }

    private List<LoanInstallment> createInstallments(BigDecimal amount, Integer numberOfInstallments) {
        ArrayList<LoanInstallment> list = new ArrayList<>();
        LoanInstallment loanInstallment;
        LocalDate nextDate = LocalDate.now();

        for (int i = 0; i < numberOfInstallments; i++) {
            nextDate = findFirstInstallmentDate(nextDate);
            loanInstallment = createInstallment(nextDate, amount);
            list.add(loanInstallment);

        }

        return list;
    }

    private LoanInstallment createInstallment(LocalDate dueDate, BigDecimal amount) {
        return LoanInstallment.builder().amount(amount).dueDate(dueDate).paidAmount(null).paid(false).paymentDate(null).build();
    }

    private LocalDate findFirstInstallmentDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();

        if (month == 12) {
            month = 1;
            year = year + 1;
        } else {
            month = month + 1;
        }

        return LocalDate.of(year, month, 1);
    }


    public Loan findByLoanId(Long loanId) throws Exception {
        Optional<Loan> loan = loanRepository.findByLoanId(loanId);
        if (loan.isEmpty()) {
            throw new Exception("Loan not found!");
        }
        return loan.get();
    }


    private void checkLoanCondition(LoanRequest loanRequest, BigDecimal availableLimit) throws Exception {
        checkCreditAvailability(loanRequest.getLoanAmount(), availableLimit);
        checkNumberOfInstallments(loanRequest.getNumberOfInstallment());
        checkInterestRange(loanRequest.getInterestRate());
    }

    private void checkCreditAvailability(BigDecimal loanAmount, BigDecimal availableLimit) throws Exception {
        if (availableLimit.compareTo(loanAmount) < 0) throw new Exception("Limit is not enough!");
    }

    private void checkNumberOfInstallments(Integer numberOfInstallment) throws Exception {
        if (!NUMBER_OF_INSTALLMENTS.contains(numberOfInstallment))
            throw new Exception("Installments should be 6, 9, 12, 24 !");
    }

    private void checkInterestRange(BigDecimal interestRate) throws Exception {
        if (interestRate.compareTo(new BigDecimal("0.1")) < 0 || interestRate.compareTo(new BigDecimal("0.5")) > 0)
            throw new Exception("Interest rate is not between 0.1 and 0.5 !");
    }


    public void payLoan(Long loanId, BigDecimal paymentAmount) throws Exception {
        Loan loan = findByLoanId(loanId);
        if (loan.isPaid()) throw new Exception("Loan is paid!");

        if (paymentAmount.compareTo(loan.getLoanInstallments().get(0).getAmount()) < 0)
            throw new Exception("The payment amount entered is not sufficient!");

        payInstallments(loan, paymentAmount);

    }

    public void payInstallments(Loan loan, BigDecimal paymentAmount) {

        payInstallment(paymentAmount, 0, loan.getNumberOfInstallment(), loan.getLoanInstallments(), 0, 3);

        if (checkLoanIsPaid(loan)) {
            loan.setPaid(true);
            loanRepository.save(loan);
        }

    }

    public void payInstallment(BigDecimal paymentAmount, int current, int numberOfInstallmentsCounter, List<LoanInstallment> loanInstallmentList, int paidCounter, int totalPaidCounter) {

        if (!loanInstallmentList.get(current).isPaid()) {

            BigDecimal calculatedPaymentAmount = calculatePayment(loanInstallmentList.get(current).getAmount(), loanInstallmentList.get(current).getDueDate());

            loanInstallmentList.get(current).setPaidAmount(calculatedPaymentAmount);
            loanInstallmentList.get(current).setPaymentDate(LocalDate.now());
            loanInstallmentList.get(current).setPaid(true);

            loanInstallmentRepository.save(loanInstallmentList.get(current));

            paymentAmount = paymentAmount.subtract(calculatedPaymentAmount);
            paidCounter++;
        }
        current++;
        numberOfInstallmentsCounter--;

        if (numberOfInstallmentsCounter > 0 && paidCounter < totalPaidCounter && paymentAmount.compareTo(loanInstallmentList.get(current).getAmount()) >= 0) {
            payInstallment(paymentAmount, current, numberOfInstallmentsCounter, loanInstallmentList, paidCounter, totalPaidCounter);
        }
    }

    private BigDecimal calculatePayment(BigDecimal fixedAmount, LocalDate dueDate) {
        long daysBetween = DAYS.between(LocalDate.now(), dueDate);

        BigDecimal calculatedAmount = new BigDecimal(daysBetween).multiply(new BigDecimal("0.001")).multiply(fixedAmount);

        if (daysBetween < 0) {
            calculatedAmount = fixedAmount.add(calculatedAmount);
        } else {
            calculatedAmount = fixedAmount.subtract(calculatedAmount);
        }

        return calculatedAmount;
    }

    private boolean checkLoanIsPaid(Loan loan) {
        return loan.getLoanInstallments().stream().filter(LoanInstallment::isPaid).toList().size() == loan.getNumberOfInstallment();
    }


    public List<Loan> findAllLoan() {
        return loanRepository.findAll();
    }
}
