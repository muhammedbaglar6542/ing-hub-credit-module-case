package com.inghub.creditmodule.controller;

import com.inghub.creditmodule.model.LoanInstallment;
import com.inghub.creditmodule.service.LoanInstallmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loanInstallment")
@Slf4j
public class LoanInstallmentController {

    private final LoanInstallmentService loanInstallmentService;

    public LoanInstallmentController(LoanInstallmentService loanInstallmentService) {
        this.loanInstallmentService = loanInstallmentService;
    }


    @GetMapping("/findByInstallment/{loanInstallmentId}")
    public ResponseEntity<Optional<LoanInstallment>> findByLoanInstallmentId(@PathVariable Long loanInstallmentId) {
        log.info("Fetching loan installment");
        return ResponseEntity.ok(loanInstallmentService.findByLoanInstallmentId(loanInstallmentId));
    }

    @GetMapping("/findByLoan/{loanId}")
    public ResponseEntity<Optional<List<LoanInstallment>>> findByLoanId(@PathVariable Long loanId) {
        log.info("Fetching loan");
        return ResponseEntity.ok(loanInstallmentService.findByLoanId(loanId));
    }

}
