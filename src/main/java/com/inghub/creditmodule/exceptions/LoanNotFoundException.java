package com.inghub.creditmodule.exceptions;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException() {
        super("Loan not found!");
    }

}
