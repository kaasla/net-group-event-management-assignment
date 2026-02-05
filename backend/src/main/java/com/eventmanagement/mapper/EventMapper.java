package com.eventmanagement.mapper;

import com.eventmanagement.dto.EventResponse;
import com.eventmanagement.entity.Event;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {

    public static EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDateTime(),
                event.getMaxParticipants(),
                event.getParticipantCount(),
                event.getCreatedAt()
        );
    }
}

