package com.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByEventIdOrderByCreatedAtAsc(Long eventId);

    boolean existsByEventIdAndPersonalCode(Long eventId, String personalCode);
}
