package com.inghub.creditmodule.controller;

import com.inghub.creditmodule.model.Loan;
import com.inghub.creditmodule.request.LoanRequest;
import com.inghub.creditmodule.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loan")
@Slf4j
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createLoan(@RequestBody LoanRequest loanRequest) {
        try {
            loanService.createLoan(loanRequest);
            return ResponseEntity.ok("Loan created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find/{loanId}")
    public ResponseEntity<Loan> findByLoanId(@PathVariable Long loanId) throws Exception {
        log.info("Fetching loan");

        return ResponseEntity.ok(loanService.findByLoanId(loanId));
    }

    @PostMapping("/pay/{loanId}/{paymentAmount}")
    public ResponseEntity<String> payLoan(@PathVariable Long loanId, @PathVariable BigDecimal paymentAmount) {
        try {
            loanService.payLoan(loanId, paymentAmount);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<Loan>> findAllLoan() throws Exception {
        log.info("Fetching  all loan");
        return ResponseEntity.ok(loanService.findAllLoan());
    }
}
