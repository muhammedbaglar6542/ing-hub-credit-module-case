package com.inghub.creditmodule.exceptions;

public class LoanIsPaidException extends RuntimeException {

    public LoanIsPaidException() {
        super("Loan is paid!");
    }

}
