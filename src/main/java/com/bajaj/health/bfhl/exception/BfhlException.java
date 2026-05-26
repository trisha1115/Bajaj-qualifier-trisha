package com.bajaj.health.bfhl.exception;

/**
 * Custom runtime exception representing errors in BFHL application processing.
 */
public class BfhlException extends RuntimeException {

    public BfhlException(String message) {
        super(message);
    }

    public BfhlException(String message, Throwable cause) {
        super(message, cause);
    }
}
