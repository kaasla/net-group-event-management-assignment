package com.eventmanagement.mapper;

import com.eventmanagement.dto.ParticipantResponse;
import com.eventmanagement.entity.Participant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParticipantMapper {

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
