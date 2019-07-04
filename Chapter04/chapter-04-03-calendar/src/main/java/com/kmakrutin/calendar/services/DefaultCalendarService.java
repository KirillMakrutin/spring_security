package com.kmakrutin.calendar.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.domain.Event;
import com.kmakrutin.calendar.repositories.CalendarUserAuthorityRepository;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.repositories.EventRepository;
import com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCalendarService implements CalendarService
{
  private final CalendarUserRepository calendarUserRepository;
  private final CalendarUserAuthorityRepository calendarUserAuthorityRepository;
  private final EventRepository eventRepository;
  private final PasswordEncoder passwordEncoder;

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
    user.setPassword( passwordEncoder.encode( user.getPassword() ) );
    // create a CalendarUser in DB
    CalendarUser savedUser = calendarUserRepository.save(user);

    CalendarUserAuthorityUtils.createDbAuthorities(savedUser).forEach(calendarUserAuthorityRepository::save);

    return savedUser;
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
