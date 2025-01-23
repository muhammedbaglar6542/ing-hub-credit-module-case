package com.inghub.creditmodule.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LoanRequest {
    private Long customerId;
    private BigDecimal loanAmount;
    private Integer numberOfInstallment;
    private LocalDate createDate;
    private BigDecimal interestRate;
}
