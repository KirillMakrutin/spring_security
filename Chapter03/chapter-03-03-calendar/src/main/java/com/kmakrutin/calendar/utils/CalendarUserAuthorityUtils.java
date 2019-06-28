package com.kmakrutin.calendar.utils;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.kmakrutin.calendar.domain.CalendarUser;

public class CalendarUserAuthorityUtils
{
  private CalendarUserAuthorityUtils()
  {
  }

  public static List<GrantedAuthority> createAuthorities( CalendarUser calendarUser )
  {
    List<GrantedAuthority> authorities;
    if ( calendarUser.getEmail().toLowerCase().startsWith( "admin" ) )

    {
      authorities = AuthorityUtils.createAuthorityList( "ROLE_USER", "ROLE_ADMIN" );
    }
    else
    {
      authorities = AuthorityUtils.createAuthorityList( "ROLE_USER" );
    }
    return authorities;
  }
}
