package org.example.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Object> handleMissingMultipartFile(MissingServletRequestPartException ex) {
        return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
