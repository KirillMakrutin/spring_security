package com.kmakrutin.calendar.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.kmakrutin.calendar.domain.CalendarUserAuthority;
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

  public static List<CalendarUserAuthority> createDbAuthorities( CalendarUser calendarUser )
  {
    return createAuthorities(calendarUser).stream().map(grantedAuthority -> {
      CalendarUserAuthority authority = new CalendarUserAuthority();
      authority.setAuthority(grantedAuthority.getAuthority());
      authority.setUser(calendarUser);
      return authority;
    }).collect(Collectors.toList());
  }

}
