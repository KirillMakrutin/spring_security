package com.kmakrutin.calendar.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.domain.Event;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.repositories.EventRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCalendarService implements CalendarService
{
  private final CalendarUserRepository calendarUserRepository;
  private final EventRepository eventRepository;

  @Override
  public CalendarUser getUser( int id )
  {
    return calendarUserRepository.getOne( id );
  }

  @Override
  public CalendarUser findUserByEmail( String email )
  {
    return calendarUserRepository.findByEmail( email );
  }

  @Override
  public List<CalendarUser> findUsersByEmail( String partialEmail )
  {
    return calendarUserRepository.findUsersByEmail( partialEmail );
  }

  @Override
  public CalendarUser createUser( CalendarUser user )
  {
    return calendarUserRepository.save( user );
  }

  @Override
  public Event getEvent( int eventId )
  {
    return eventRepository.getOne( eventId );
  }

  @Override
  public Event createEvent( Event event )
  {
    return eventRepository.save( event );
  }

  @Override
  public List<Event> findForUser( int userId )
  {
    return eventRepository.findByAttendeeIdOrOwnerId( userId, userId );
  }

  @Override
  public List<Event> getEvents()
  {
    return eventRepository.findAll();
  }
}
