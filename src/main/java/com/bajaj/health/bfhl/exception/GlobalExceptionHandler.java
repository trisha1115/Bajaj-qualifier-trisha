package com.bajaj.health.bfhl.exception;

import com.bajaj.health.bfhl.dto.BfhlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BfhlException.class)
    public ResponseEntity<BfhlResponse> handleBfhlException(BfhlException ex) {
        log.error("BfhlException: {}", ex.getMessage());
        BfhlResponse err = new BfhlResponse();
        err.setSuccess(false);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BfhlResponse> handleGeneralException(Exception ex) {
        log.error("Unhandled exception", ex);
        BfhlResponse err = new BfhlResponse();
        err.setSuccess(false);
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}