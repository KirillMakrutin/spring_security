package com.kmakrutin.calendar.services;

import static com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils.createAuthorities;

import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;

@Component
@RequiredArgsConstructor
public class SpringSecurityUserContext implements UserContext
{
  private final CalendarUserRepository userRepository;

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

    User user = (User) authentication.getPrincipal();

    return userRepository.findByEmail(user.getUsername());
  }

  @Override
  public void setCurrentUser( CalendarUser calendarUser )
  {
    User user = new User( calendarUser.getEmail(), calendarUser.getPassword(), CalendarUserAuthorityUtils.createAuthorities(calendarUser) );
    Authentication authentication = new UsernamePasswordAuthenticationToken( user, user.getPassword(), user.getAuthorities() );
    SecurityContextHolder.getContext().setAuthentication( authentication );
  }
}
