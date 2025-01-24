package com.inghub.creditmodule.exceptions;

public class LimitIsNotEnoughException extends RuntimeException {

    public LimitIsNotEnoughException() {
        super("Limit is not enough!");
    }

}
