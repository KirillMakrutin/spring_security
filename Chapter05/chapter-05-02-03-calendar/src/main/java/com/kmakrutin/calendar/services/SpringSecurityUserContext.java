package com.kmakrutin.calendar.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringSecurityUserContext implements UserContext
{
  private final CalendarUserDetailsService calendarUserDetailsService;

  @Override
  public CalendarUser getCurrentUser()
  {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();

    // can be null if not logged in
    if ( authentication == null )
    {
      return null;
    }

    return ( CalendarUser ) authentication.getPrincipal();
  }

  @Override
  public void setCurrentUser( CalendarUser calendarUser )
  {
    UserDetails user = calendarUserDetailsService.loadUserByUsername( calendarUser.getEmail() );
    Authentication authentication = new UsernamePasswordAuthenticationToken( user, user.getPassword(), user.getAuthorities() );
    SecurityContextHolder.getContext().setAuthentication( authentication );
  }
}
