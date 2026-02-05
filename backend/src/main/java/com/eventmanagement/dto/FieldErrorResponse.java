package com.eventmanagement.dto;

public record FieldErrorResponse(
        String field,
        String message
) {
}
