package com.kmakrutin.calendar.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.kmakrutin.calendar.domain.CalendarUser;

/**
 * When a user authenticates, Spring Security submits an Authentication object to
 * AuthenticationProvider with the information provided by the user. The current
 * UsernamePasswordAuthentication object only contains a username and password field.
 * Create a DomainUsernamePasswordAuthenticationToken object that contains a domain
 * field
 */
public class DomainUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken
{
  private final String domain;

  /**
   * Used for attempting authentication
   */
  public DomainUsernamePasswordAuthenticationToken( String username, String password, String domain )
  {
    super( username, password );
    this.domain = domain;
  }

  /**
   * Used for returning to Spring Security after being authenticated
   */
  public DomainUsernamePasswordAuthenticationToken( CalendarUser principal, String password, String domain, Collection<? extends GrantedAuthority> authorities )
  {
    super( principal, password, authorities );
    this.domain = domain;
  }

  public String getDomain()
  {
    return domain;
  }
}
