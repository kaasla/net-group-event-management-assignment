package com.eventmanagement.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for creating a new event")
public record CreateEventRequest(
        @NotBlank(message = "Event name is required")
        @Size(max = 100, message = "Event name must not exceed 100 characters")
        @Schema(description = "Name of the event", example = "Spring Conference 2026")
        String name,

        @NotNull(message = "Event date and time is required")
        @Future(message = "Event date must be in the future")
        @Schema(description = "Date and time of the event in ISO-8601 format", example = "2026-06-15T18:00:00Z")
        Instant dateTime,

        @NotNull(message = "Maximum participants is required")
        @Min(value = 1, message = "Maximum participants must be at least 1")
        @Max(value = 10000, message = "Maximum participants must not exceed 10000")
        @Schema(description = "Maximum number of participants allowed", example = "100")
        Integer maxParticipants
) {
}
