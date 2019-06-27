package com.kmakrutin.calendar.services;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
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
  /**
   * @see com.kmakrutin.calendar.configuration.SecurityConfig where we exposed {@link UserDetailsManager} to aware Spring
   * Security Context of a new user
   */
  private final UserDetailsManager userDetailsManager;
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
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList( "ROLE_USER" );
    UserDetails userDetails = new User( user.getEmail(), user.getPassword(), authorities );
    // create a Spring Security user
    userDetailsManager.createUser( userDetails );
    // create a CalendarUser in DB
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
