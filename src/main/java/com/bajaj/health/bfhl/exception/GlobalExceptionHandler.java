package com.bajaj.health.bfhl.exception;

import com.bajaj.health.bfhl.dto.BfhlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller advice class intercepting application exceptions and mapping them to standard responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Intercepts BfhlExceptions representing bad request inputs or validation errors.
     *
     * @param ex the captured business exception
     * @return a response wrapper containing false success and HTTP 400 Bad Request status
     */
    @ExceptionHandler(BfhlException.class)
    public ResponseEntity<BfhlResponse> handleBfhlException(BfhlException ex) {
        log.error("Validation error resolved in advice layer: {}", ex.getMessage());
        BfhlResponse errorResponse = new BfhlResponse();
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fallback exception interceptor capturing general unexpected anomalies.
     *
     * @param ex the captured general exception
     * @return a response wrapper containing false success and HTTP 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BfhlResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected anomaly caught in global advice context", ex);
        BfhlResponse errorResponse = new BfhlResponse();
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
