package com.eventmanagement.exception;

/**
 * Thrown when admin login credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
