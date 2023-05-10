package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @Hidden
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessage resourceNotFound(ResourceNotFoundException ex) {
        return new ResponseMessage(404, "NOT_EXIST", ex.getMessage());
    }

    @Hidden
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage missingParameter(MissingServletRequestParameterException ex) {
        return new ResponseMessage(400, "MISSING_PARAMETER", ex.getMessage());
    }

}
