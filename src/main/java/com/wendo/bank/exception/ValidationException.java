package com.wendo.bank.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Object ...ids) {
        super(String.format(message, ids));
    }
}
