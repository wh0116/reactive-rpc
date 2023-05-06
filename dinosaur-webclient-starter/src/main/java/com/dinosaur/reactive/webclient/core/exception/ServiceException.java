package com.dinosaur.reactive.webclient.core.exception;

public class ServiceException extends RuntimeException{
    private int statusCode;

    public ServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
