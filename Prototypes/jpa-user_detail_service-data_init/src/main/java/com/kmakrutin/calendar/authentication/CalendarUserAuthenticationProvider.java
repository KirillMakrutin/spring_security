package com.kmakrutin.calendar.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Throughout the rest of this section, we are going to create a custom AuthenticationProvider object named
 * CalendarUserAuthenticationProvider that will replace CalendarUserDetailsService. Then, we will use
 * CalendarUserAuthenticationProvider to consider an additional parameter to support authenticating users from multiple domains.
 *
 * We must use an AuthenticationProvider object rather than UserDetailsService, because the UserDetails interface has no
 * concept of a domain parameter.
 */
//@Component
@AllArgsConstructor
@Slf4j
public class CalendarUserAuthenticationProvider implements AuthenticationProvider
{
  private final CalendarUserRepository calendarUserRepository;

  @Override
  public Authentication authenticate( Authentication authentication ) throws AuthenticationException
  {
    DomainUsernamePasswordAuthenticationToken token = ( DomainUsernamePasswordAuthenticationToken ) authentication;
    String username = token.getName();
    String domain = token.getDomain();

    if ( username == null || domain == null )
    {
      throw new UsernameNotFoundException( "Invalid username/password" );
    }

    CalendarUser user = calendarUserRepository.findByEmail( username + '@' + domain );

    if ( user == null )
    {
      throw new UsernameNotFoundException( "Invalid username/password" );
    }

    String password = user.getPassword();
    if ( !token.getCredentials().equals( password ) )
    {
      throw new BadCredentialsException( "Invalid username/password" );
    }

    return new DomainUsernamePasswordAuthenticationToken( user, password, domain, CalendarUserAuthorityUtils.createAuthorities( user ) );
  }

  @Override
  public boolean supports( Class<?> authentication )
  {
    return DomainUsernamePasswordAuthenticationToken.class.equals( authentication );
  }
}
