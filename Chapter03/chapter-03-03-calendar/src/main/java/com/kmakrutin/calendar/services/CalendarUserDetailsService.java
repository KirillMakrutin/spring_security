package com.kmakrutin.calendar.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarUserDetailsService implements UserDetailsService
{
  private final CalendarUserRepository calendarUserRepository;

  @Override
  public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
  {
    CalendarUser calendarUser = calendarUserRepository.findByEmail( username );

    if ( calendarUser == null )
    {
      log.error( "User {} not found.", username );

      return null;
    }

    return new User( calendarUser.getEmail(), calendarUser.getPassword(), CalendarUserAuthorityUtils.createAuthorities( calendarUser ) );
  }
}
