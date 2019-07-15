package com.kmakrutin.calendar.authentication;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.services.CalendarService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProviderConnectionSignUp implements ConnectionSignUp
{
  private final CalendarService calendarService;

  @Override
  public String execute( Connection<?> connection )
  {
    UserProfile userProfile = connection.fetchUserProfile();
    CalendarUser calendarUser = new CalendarUser();

    if ( userProfile.getEmail() != null )
    {
      calendarUser.setEmail( userProfile.getEmail() );
    }
    else if ( userProfile.getUsername() != null )
    {
      calendarUser.setEmail( userProfile.getUsername() );
    }
    else
    {
      calendarUser.setEmail( connection.getDisplayName() );
    }

    calendarUser.setFirstName( userProfile.getFirstName() );
    calendarUser.setLastName( userProfile.getLastName() );
    calendarUser.setPassword( randomAlphabetic( 32 ) );
    calendarUser = calendarService.createUser( calendarUser );

    return calendarUser.getEmail();
  }
}
