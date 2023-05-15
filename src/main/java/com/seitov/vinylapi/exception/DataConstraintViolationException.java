package com.seitov.vinylapi.exception;

public class DataConstraintViolationException extends RuntimeException {

    public DataConstraintViolationException(String errorMessage) {
        super(errorMessage);
    }

}
