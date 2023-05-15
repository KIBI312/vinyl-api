package com.seitov.vinylapi.exception;

public class RedundantPropertyException extends RuntimeException {

    public RedundantPropertyException(String errorMessage) {
        super(errorMessage);
    }

}
