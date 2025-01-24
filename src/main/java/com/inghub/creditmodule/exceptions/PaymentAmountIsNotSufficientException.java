package com.inghub.creditmodule.exceptions;

public class PaymentAmountIsNotSufficientException extends RuntimeException {

    public PaymentAmountIsNotSufficientException() {
        super("The payment amount entered is not sufficient!");
    }

}

