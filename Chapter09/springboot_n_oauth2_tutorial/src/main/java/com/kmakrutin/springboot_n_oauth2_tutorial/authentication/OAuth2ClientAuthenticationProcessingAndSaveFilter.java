package com.kmakrutin.springboot_n_oauth2_tutorial.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.kmakrutin.springboot_n_oauth2_tutorial.model.OAuthUser;
import com.kmakrutin.springboot_n_oauth2_tutorial.service.OAuthUserService;

public class OAuth2ClientAuthenticationProcessingAndSaveFilter extends OAuth2ClientAuthenticationProcessingFilter
{
  private final OAuthUserService userService;

  public OAuth2ClientAuthenticationProcessingAndSaveFilter( OAuthUserService userService, String defaultFilterProcessesUrl )
  {
    super( defaultFilterProcessesUrl );

    this.userService = userService;
  }

  @Override
  protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult ) throws IOException, ServletException
  {
    super.successfulAuthentication( request, response, chain, authResult );
    SecurityContext context = SecurityContextHolder.getContext();

    OAuth2Authentication authentication = ( OAuth2Authentication ) context.getAuthentication();
    OAuthUser user = ( OAuthUser ) userService.loadUserByUsername( authentication.getName() );
    if ( user == null )
    {
      user = new OAuthUser();
      user.setName( authentication.getName() );
      user.setUsername( authentication.getName() );

      user = userService.saveOrUpdateUser( user );
    }

    context.setAuthentication( new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities() ) );
  }
}
