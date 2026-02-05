package com.eventmanagement.dto;

import java.time.Instant;

/**
 * Response payload representing a registered participant.
 */
public record ParticipantResponse(
        Long id,
        String firstName,
        String lastName,
        String personalCode,
        Instant createdAt
) {
}
