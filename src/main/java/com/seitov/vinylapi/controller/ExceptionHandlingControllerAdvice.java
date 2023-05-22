package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @Hidden
    @ExceptionHandler(value = {RedundantPropertyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage redundantProperty(RedundantPropertyException ex) {
        return new ResponseMessage(400, "REDUNDANT_PROPERTY", ex.getMessage());
    }

    @Hidden
    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage resourceDuplication(ResourceAlreadyExistsException ex) {
        return new ResponseMessage(400, "RESOURCE_DUPLICATION", ex.getMessage());
    }

    @Hidden
    @ExceptionHandler(value = {DataConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseMessage dataConstrainViolation(DataConstraintViolationException ex) {
        return new ResponseMessage(409, "DATA_CONSTRAINT_VIOLATION", ex.getMessage());
    }

    @Hidden
    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage serverError(RuntimeException ex) {
        return new ResponseMessage(500, "SERVER_ERROR", ex.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage resourceInvalidFromat(MethodArgumentNotValidException ex) {
        return new ResponseMessage(400, "INVALID_PROPERTY", ex.getAllErrors().get(0).getDefaultMessage());
    }

}
