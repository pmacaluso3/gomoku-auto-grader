package org.example.controllers;

import org.example.domain.Result;
import org.example.domain.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerHelper {

    public static ResponseEntity<Object> convertResultToHttpResponse(Result result) {
        if (result.getResultType() == ResultType.INVALID) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
    }
}
