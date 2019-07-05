package com.kmakrutin.calendar.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.domain.Event;
import com.kmakrutin.calendar.domain.Role;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.repositories.EventRepository;
import com.kmakrutin.calendar.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCalendarService implements CalendarService
{
  private final CalendarUserRepository calendarUserRepository;
  private final EventRepository eventRepository;
  private final RoleRepository roleRepository;
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

    Set<Role> roles = new HashSet<>();
    roles.add( roleRepository.findByName( "ROLE_USER" ) );

    if ( user.getEmail().toLowerCase().startsWith( "admin" ) )
    {
      roles.add( roleRepository.findByName( "ROLE_ADMIN" ) );
    }

    user.setRoles( roles );

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
