package com.kmakrutin.calendar.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Created to aware Spring of domain and {@link DomainUsernamePasswordAuthenticationToken}
 * <br/>
 * formLogin() method instructed Spring Security to use o.s.s.web.authentication.UsernamePasswordAuthenticationFilter to act
 * as a login controller. The filter's job is to perform the following tasks:
 * <ul>
 * <li>Obtain a username and password from the HTTP request.</li>
 * <li>Create a UsernamePasswordAuthenticationToken object with the information
 * obtained from the HTTP request.
 * </li>
 * <li>Request that Spring Security validates UsernamePasswordAuthenticationToken.</li>
 * <li>If the token is validated, it will set the authentication returned to it on
 * SecurityContextHolder, just as we did when a new user signed up for an
 * account. We will need to extend UsernamePasswordAuthenticationFilter to
 * leverage our newly created DoainUsernamePasswordAuthenticationToken
 * object.
 * </li>
 * </ul>
 */
public class DomainUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
  public DomainUsernamePasswordAuthenticationFilter( AuthenticationManager authenticationManager )
  {
    setAuthenticationManager( authenticationManager );
  }

  @Override
  public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException
  {
    if ( !request.getMethod().equals( "POST" ) )
    {
      throw new AuthenticationServiceException( "Authentication method not supported " + request.getMethod() );
    }

    String username = obtainUsername( request );
    String password = obtainPassword( request );
    String domain = request.getParameter( "domain" );

    DomainUsernamePasswordAuthenticationToken token = new DomainUsernamePasswordAuthenticationToken( username, password, domain );
    setDetails( request, token );

    // manager will iterate all authentication providers and chooses the one that support DomainUsernamePasswordAuthenticationToken
    return this.getAuthenticationManager().authenticate( token );
  }
}
