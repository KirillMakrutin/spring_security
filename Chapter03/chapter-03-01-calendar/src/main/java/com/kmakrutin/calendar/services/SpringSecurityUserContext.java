package com.kmakrutin.calendar.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringSecurityUserContext implements UserContext
{
  private final CalendarService calendarService;

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

    return calendarService.findUserByEmail( authentication.getName() );
  }

  @Override
  public void setCurrentUser( CalendarUser user )
  {
    throw new UnsupportedOperationException();
  }
}
