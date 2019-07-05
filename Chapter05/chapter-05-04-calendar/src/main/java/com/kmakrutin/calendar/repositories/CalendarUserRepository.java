package com.kmakrutin.calendar.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kmakrutin.calendar.domain.CalendarUser;

public interface CalendarUserRepository extends MongoRepository<CalendarUser, Integer>
{
  CalendarUser findByEmail( String email );
}
