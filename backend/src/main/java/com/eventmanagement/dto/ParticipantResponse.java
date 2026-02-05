package com.eventmanagement.dto;

import java.time.Instant;

public record ParticipantResponse(
        Long id,
        String firstName,
        String lastName,
        String personalCode,
        Instant createdAt
) {
}
