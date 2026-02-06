package com.eventmanagement.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Event details with participant statistics")
public record EventResponse(
        @Schema(description = "Unique event identifier", example = "1")
        Long id,

        @Schema(description = "Name of the event", example = "Spring Conference 2026")
        String name,

        @Schema(description = "Date and time of the event", example = "2026-06-15T18:00:00Z")
        Instant dateTime,

        @Schema(description = "Maximum number of participants allowed", example = "100")
        int maxParticipants,

        @Schema(description = "Current number of registered participants", example = "42")
        int participantCount,

        @Schema(description = "Number of remaining spots available", example = "58")
        int availableSpots,

        @Schema(description = "Timestamp when the event was created", example = "2026-01-15T10:30:00Z")
        Instant createdAt
) {
}
