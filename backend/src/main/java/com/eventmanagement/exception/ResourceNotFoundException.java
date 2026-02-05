package com.eventmanagement.exception;

/**
 * Thrown when a requested resource (e.g., event) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException event(Long id) {
        return new ResourceNotFoundException("Event not found with id: " + id);
    }
}
