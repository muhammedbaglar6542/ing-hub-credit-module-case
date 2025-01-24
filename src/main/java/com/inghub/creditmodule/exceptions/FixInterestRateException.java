package com.inghub.creditmodule.exceptions;

public class FixInterestRateException extends RuntimeException {

    public FixInterestRateException() {
        super("Interest rate is not between 0.1 and 0.5 !");
    }

}
