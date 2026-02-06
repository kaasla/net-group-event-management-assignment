package com.eventmanagement.exception;

/**
 * Thrown when a person attempts to register for an event they're already registered for.
 */
public class DuplicateRegistrationException extends RuntimeException {

    public DuplicateRegistrationException(String personalCode, Long eventId) {
        super("Person with code %s is already registered for event %d".formatted(personalCode, eventId));
    }
}
