package com.eventmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Individual field validation error")
public record FieldErrorResponse(
        @Schema(description = "Name of the field that failed validation", example = "firstName")
        String field,

        @Schema(description = "Validation error message", example = "First name is required")
        String message
) {
}
