package com.kmakrutin.calendar.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kmakrutin.calendar.domain.Event;

public interface EventRepository extends MongoRepository<Event, Integer>
{
  List<Event> findByAttendeeIdOrOwnerId( Integer attendeeId, Integer ownerId );
}
