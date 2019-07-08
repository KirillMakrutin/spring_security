package com.kmakrutin.calendar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmakrutin.calendar.domain.Event;

public interface EventRepository extends JpaRepository<Event, Integer>
{
  List<Event> findByAttendeeIdOrOwnerId( Integer attendeeId, Integer ownerId );
}
