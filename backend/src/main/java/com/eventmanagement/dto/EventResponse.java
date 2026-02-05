package com.eventmanagement.dto;

import java.time.Instant;

public record EventResponse(
        Long id,
        String name,
        Instant dateTime,
        int maxParticipants,
        int participantCount,
        Instant createdAt
) {
}
