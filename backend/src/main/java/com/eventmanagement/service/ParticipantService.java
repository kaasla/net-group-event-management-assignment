package com.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventmanagement.dto.ParticipantResponse;
import com.eventmanagement.dto.RegistrationRequest;
import com.eventmanagement.entity.Event;
import com.eventmanagement.exception.DuplicateRegistrationException;
import com.eventmanagement.exception.EventFullException;
import com.eventmanagement.exception.ResourceNotFoundException;
import com.eventmanagement.mapper.ParticipantMapper;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    @Transactional
    public ParticipantResponse registerParticipant(Long eventId, RegistrationRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> ResourceNotFoundException.event(eventId));

        validateCapacity(event);
        validateNoDuplicate(eventId, request.personalCode());

        return Optional.of(ParticipantMapper.toEntity(event, request))
                .map(participantRepository::save)
                .map(ParticipantMapper::toResponse)
                .orElseThrow();
    }

    public List<ParticipantResponse> getParticipantsByEventId(Long eventId) {
        return Optional.of(eventId)
                .filter(eventRepository::existsById)
                .map(participantRepository::findAllByEventIdOrderByCreatedAtAsc)
                .orElseThrow(() -> ResourceNotFoundException.event(eventId))
                .stream()
                .map(ParticipantMapper::toResponse)
                .toList();
    }

    private void validateCapacity(Event event) {
        if (event.isFull()) {
            throw new EventFullException(event.getId());
        }
    }

    private void validateNoDuplicate(Long eventId, String personalCode) {
        if (participantRepository.existsByEventIdAndPersonalCode(eventId, personalCode)) {
            throw new DuplicateRegistrationException(personalCode, eventId);
        }
    }
}
