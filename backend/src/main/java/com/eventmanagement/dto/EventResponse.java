package com.eventmanagement.dto;

import java.time.Instant;

/**
 * Response payload representing an event.
 */
public record EventResponse(
        Long id,
        String name,
        Instant dateTime,
        int maxParticipants,
        int participantCount,
        Instant createdAt
) {
}
