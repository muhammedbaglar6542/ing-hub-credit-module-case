package com.inghub.creditmodule.exceptions;

public class FixNumberOfInstallmentException extends RuntimeException {

    public FixNumberOfInstallmentException() {
        super("Installments should be 6, 9, 12, 24 !");
    }

}
