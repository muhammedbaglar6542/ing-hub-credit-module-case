package com.inghub.creditmodule.service;

import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.repository.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;

    public Optional<List<LoanInstallment>> findByLoanId(Long loanId) {
        return loanInstallmentRepository.findByLoanId(loanId);
    }

    public Optional<LoanInstallment> findByLoanInstallmentId(Long loanId) {
        return loanInstallmentRepository.findByInstallmentId(loanId);
    }

    public List<LoanInstallment> findAllLoanInstallments() {
        return loanInstallmentRepository.findAll();
    }
}
