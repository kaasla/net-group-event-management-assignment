package com.eventmanagement.exception;

/**
 * Thrown when attempting to register for an event that has reached capacity.
 */
public class EventFullException extends RuntimeException {

    public EventFullException(Long eventId) {
        super("Event with id %d has reached maximum capacity".formatted(eventId));
    }
}
