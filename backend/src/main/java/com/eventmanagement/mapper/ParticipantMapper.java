package com.eventmanagement.mapper;

import com.eventmanagement.dto.ParticipantResponse;
import com.eventmanagement.dto.RegistrationRequest;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Participant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParticipantMapper {

    public static Participant toEntity(Event event, RegistrationRequest request) {
        return Participant.builder()
                .event(event)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .personalCode(request.personalCode())
                .build();
    }

    public static ParticipantResponse toResponse(Participant participant) {
        return new ParticipantResponse(
                participant.getId(),
                participant.getFirstName(),
                participant.getLastName(),
                participant.getPersonalCode(),
                participant.getCreatedAt()
        );
    }
}
