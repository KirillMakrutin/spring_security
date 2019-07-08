package com.kmakrutin.calendar.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

// PersistentTokenBasedRememberMeServices already improved RememberMeServices using Barry Jaspan's article
public class CalendarPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices
{
  public CalendarPersistentTokenBasedRememberMeServices( String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository )
  {
    super( key, userDetailsService, tokenRepository );
  }
}
