package com.kmakrutin.calendar.services;

import com.kmakrutin.calendar.domain.CalendarUser;

/**
 * <p>
 * Like most applications, our application requires us to interact with the currently logged-in user.
 * </p><br/>
 * <p>
 * Spring Security provides quite a few different methods for authenticating a user. However, the net result is that Spring
 * Security will populate <b>o.s.s.core.context.SecurityContext</b> with <b>o.s.s.core.Authentication</b>. The Authentication object
 * represents all the information we gathered at the time of authentication (username, password, roles, and so on).
 * </p><br/>
 * <p>
 * The SecurityContext interface then set on the <b>o.s.s.core.context.SecurityContextHolder</b> interface.
 * </p><br/>
 * <p>
 * This means that Spring Security and developers can use SecurityContextHolder to obtain information about the currently
 * logged-in user.
 * </p><br/>
 * <p>
 * An example of obtaining the current username is illustrated as follows:
 * </p><br/>
 * <code>
 *    String username = SecurityContextHolder.getContext().getAuthentication().getName();
 * </code><br/><br/>
 * It should be noted that null checks should always be done on the Authentication object, as this could be null if the
 * user is not logged in.
 */
public interface UserContext
{
  /**
   * Gets the currently logged in {@link CalendarUser} or null if there is no authenticated user.
   */
  CalendarUser getCurrentUser();

  /**
   * Sets the currently logged in {@link CalendarUser}.
   *
   * @param user
   *     the logged in {@link CalendarUser}. Cannot be null.
   * @throws IllegalArgumentException
   *     if the {@link CalendarUser} is null.
   */
  void setCurrentUser( CalendarUser user );
}
