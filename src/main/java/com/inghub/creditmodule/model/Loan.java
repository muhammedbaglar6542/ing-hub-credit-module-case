package com.inghub.creditmodule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "loan")
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;
    private Long customerId;
    private BigDecimal loanAmount;
    private Integer numberOfInstallment;
    private LocalDate createDate;
    private boolean paid;

    @JsonIgnore
    @ManyToOne
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "loanId", referencedColumnName = "loanId")
    private List<LoanInstallment> loanInstallments;
}
