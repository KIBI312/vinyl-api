package com.seitov.vinylapi.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

}
