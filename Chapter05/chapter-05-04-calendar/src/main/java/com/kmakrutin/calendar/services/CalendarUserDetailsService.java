package com.kmakrutin.calendar.services;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.utils.CalendarUserAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarUserDetailsService implements UserDetailsService
{
  private final CalendarUserRepository calendarUserRepository;

  @Override
  public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
  {
    CalendarUser calendarUser = calendarUserRepository.findByEmail( username );

    if ( calendarUser == null )
    {
      log.error( "User {} not found.", username );

      throw new UsernameNotFoundException( "Incorrect username or password." );
    }

    return new CalendarUserDetails( calendarUser );
  }

  private final class CalendarUserDetails extends CalendarUser implements UserDetails
  {
    private CalendarUserDetails( CalendarUser user )
    {
      super(
          user.getId(),
          user.getFirstName(),
          user.getLastName(),
          user.getEmail(),
          user.getPassword()
      );

      setRoles( user.getRoles() );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
      return CalendarUserAuthorityUtils.createAuthorities( this );
    }

    @Override
    public String getUsername()
    {
      return getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
      return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
      return true;
    }

    @Override
    public boolean isEnabled()
    {
      return true;
    }
  }
}
