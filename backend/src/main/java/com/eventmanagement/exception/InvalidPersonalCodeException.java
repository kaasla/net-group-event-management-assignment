package com.eventmanagement.exception;

/**
 * Thrown when an Estonian personal code fails validation.
 */
public class InvalidPersonalCodeException extends RuntimeException {

    public InvalidPersonalCodeException(String personalCode) {
        super("Invalid Estonian personal code: %s".formatted(personalCode));
    }
}
