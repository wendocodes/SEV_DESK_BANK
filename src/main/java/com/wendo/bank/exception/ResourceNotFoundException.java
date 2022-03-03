package com.wendo.bank.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message, Object ...ids) {
        super(String.format(message, ids));
    }
}
