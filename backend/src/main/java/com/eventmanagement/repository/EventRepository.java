package com.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventmanagement.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByDateTimeAsc();
}
