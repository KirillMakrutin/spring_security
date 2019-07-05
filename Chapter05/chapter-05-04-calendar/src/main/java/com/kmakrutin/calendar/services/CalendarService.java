package com.kmakrutin.calendar.services;

import java.util.List;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.domain.Event;

public interface CalendarService
{
  CalendarUser getUser( int id );

  CalendarUser findUserByEmail( String email );

  CalendarUser createUser( CalendarUser user );

  Event getEvent( int eventId );

  Event createEvent( Event event );

  List<Event> findForUser( int userId );

  List<Event> getEvents();
}
