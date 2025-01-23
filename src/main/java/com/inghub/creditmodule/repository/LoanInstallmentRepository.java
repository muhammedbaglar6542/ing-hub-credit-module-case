package com.inghub.creditmodule.repository;

import com.inghub.creditmodule.model.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    Optional<LoanInstallment> findByInstallmentId(Long installmentId);

    Optional<List<LoanInstallment>> findByLoanId(Long loanId);


}
