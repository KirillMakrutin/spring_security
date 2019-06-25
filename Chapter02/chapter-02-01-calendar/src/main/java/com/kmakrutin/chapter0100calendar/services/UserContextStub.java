package com.kmakrutin.chapter0100calendar.services;

import org.springframework.stereotype.Service;

import com.kmakrutin.chapter0100calendar.domain.CalendarUser;
import com.kmakrutin.chapter0100calendar.repositories.CalendarUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserContextStub implements UserContext
{
  private final CalendarUserRepository userRepository;
  /**
   * The {@link CalendarUser#getId()} for the user that is representing the currently logged in user. This can be
   * modified using {@link #setCurrentUser(CalendarUser)}
   */
  private int currentUserId = 0;

  @Override
  public CalendarUser getCurrentUser()
  {
    return userRepository.getOne( currentUserId );
  }

  @Override
  public void setCurrentUser( CalendarUser user )
  {
    if ( user == null )
    {
      throw new IllegalArgumentException( "user cannot be null" );
    }

    Integer currentId = user.getId();

    if ( currentId == null )
    {
      throw new IllegalArgumentException( "user.getId() cannot be null" );
    }

    this.currentUserId = currentId;
  }
}
