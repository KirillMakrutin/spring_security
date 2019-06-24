package com.kmakrutin.chapter0100calendar.services;

import java.util.List;

import com.kmakrutin.chapter0100calendar.domain.CalendarUser;
import com.kmakrutin.chapter0100calendar.domain.Event;

public interface CalendarService
{
  CalendarUser getUser( int id );

  CalendarUser findUserByEmail( String email );

  List<CalendarUser> findUsersByEmail( String partialEmail );

  CalendarUser createUser( CalendarUser user );

  Event getEvent( int eventId );

  Event createEvent( Event event );

  List<Event> findForUser( int userId );

  List<Event> getEvents();
}
