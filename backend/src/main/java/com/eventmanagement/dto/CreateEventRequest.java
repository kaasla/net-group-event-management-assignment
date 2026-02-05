package com.eventmanagement.dto;

import java.time.Instant;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEventRequest(
        @NotBlank(message = "Event name is required")
        String name,

        @NotNull(message = "Event date and time is required")
        @Future(message = "Event date must be in the future")
        Instant dateTime,

        @Min(value = 1, message = "Maximum participants must be at least 1")
        int maxParticipants
) {
}
