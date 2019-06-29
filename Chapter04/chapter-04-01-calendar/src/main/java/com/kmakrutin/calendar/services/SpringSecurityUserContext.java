package com.kmakrutin.calendar.services;

import static com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils.createAuthorities;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;

@Component
public class SpringSecurityUserContext implements UserContext
{
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
  public void setCurrentUser( CalendarUser user )
  {
    Authentication authentication = new UsernamePasswordAuthenticationToken( user, user.getPassword(), createAuthorities( user ) );
    SecurityContextHolder.getContext().setAuthentication( authentication );
  }
}
