package com.eventmanagement.mapper;

import com.eventmanagement.dto.CreateEventRequest;
import com.eventmanagement.dto.EventResponse;
import com.eventmanagement.entity.Event;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {

    public static Event toEntity(CreateEventRequest request) {
        return Event.builder()
                .name(request.name())
                .dateTime(request.dateTime())
                .maxParticipants(request.maxParticipants())
                .build();
    }

    public static EventResponse toResponse(Event event) {
        int participantCount = event.getParticipantCount();
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDateTime(),
                event.getMaxParticipants(),
                participantCount,
                event.getMaxParticipants() - participantCount,
                event.getCreatedAt()
        );
    }
}
