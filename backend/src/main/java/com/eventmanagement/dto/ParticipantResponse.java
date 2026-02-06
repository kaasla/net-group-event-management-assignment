package com.eventmanagement.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Registered participant details")
public record ParticipantResponse(
        @Schema(description = "Unique participant identifier", example = "1")
        Long id,

        @Schema(description = "Participant's first name", example = "Mari")
        String firstName,

        @Schema(description = "Participant's last name", example = "Tamm")
        String lastName,

        @Schema(description = "Estonian personal identification code", example = "49403136515")
        String personalCode,

        @Schema(description = "Timestamp when the participant registered", example = "2026-01-20T14:30:00Z")
        Instant createdAt
) {
}
