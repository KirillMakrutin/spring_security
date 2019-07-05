package com.kmakrutin.calendar.configuration;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.domain.Event;
import com.kmakrutin.calendar.domain.Role;
import com.kmakrutin.calendar.repositories.CalendarUserRepository;
import com.kmakrutin.calendar.repositories.EventRepository;
import com.kmakrutin.calendar.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Initialize the initial data in the MongoDb
 * This replaces data.sql
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MongoDataInitializer
{
  private final RoleRepository roleRepository;
  private final CalendarUserRepository calendarUserRepository;
  private final EventRepository eventRepository;
  private CalendarUser user, admin, user2;
  private Role user_role, admin_role;

  // CalendarUsers
  {
    user = new CalendarUser( CalendarUser.nextId(), "User", "1", "user1@example.com", "38aab7ba97bd6bb2e51add1e5617eabfc8d13ec85c004e909eec4b70172437ae85e0c56e43fe51b0" );
    admin = new CalendarUser( CalendarUser.nextId(), "Admin", "1", "admin1@example.com", "98afcd6f54569da7fea7fe4b1bf79d59dd27e559d38ee75cabd796f43058ebe15f201dfd453942e0" );
    user2 = new CalendarUser( CalendarUser.nextId(), "User2", "2", "user2@example.com", "429d7af2097fb1a0a3a4050bff17d8189cb2244aef52476cad2fef3bcc7338078dbf73644494554a" );

  }

  @PostConstruct
  public void setUp()
  {
    log.info( "*******************************************************" );
    log.info( "clean the database" );

    calendarUserRepository.deleteAll();
    roleRepository.deleteAll();
    eventRepository.deleteAll();

    log.info( "seedRoles" );
    seedRoles();

    log.info( "seedCalendarUsers" );
    seedCalendarUsers();
    log.info( "seedEvents" );
    seedEvents();
    log.info( "*******************************************************" );
  }

  /**
   * -- ROLES --
   * insert into role(id, name) values (0, "ROLE_USER");
   * insert into role(id, name) values (1, "ROLE_ADMIN");
   */
  private void seedRoles()
  {
    user_role = new Role( CalendarUser.nextId(), "ROLE_USER" );
    user_role = roleRepository.save( user_role );

    admin_role = new Role( CalendarUser.nextId(), "ROLE_ADMIN" );
    admin_role = roleRepository.save( admin_role );
  }


  /**
   * Seed initial events
   */
  private void seedEvents()
  {
    // Event 1
    Event event1 = new Event(
        Event.nextId(),
        "Birthday Party",
        "This is going to be a great birthday",
        new Date(),
        user,
        admin
    );

    // Event 2
    Event event2 = new Event(
        Event.nextId(),
        "Conference Call",
        "Call with the client",
        new Date(),
        user2,
        user
    );

    // Event 3
    Event event3 = new Event(
        Event.nextId(),
        "Vacation",
        "Paragliding in Greece",
        new Date(),
        admin,
        user2
    );

    // save Event
    eventRepository.save( event1 );
    eventRepository.save( event2 );
    eventRepository.save( event3 );

    List<Event> events = eventRepository.findAll();

    log.info( "Events: {}", events );

  }


  private void seedCalendarUsers()
  {

    //        user.setRoles(new HashSet<>(Arrays.asList(user_role)));
    //        admin.setRoles(new HashSet<>(Arrays.asList(user_role, admin_role)));
    //        user2.setRoles(new HashSet<>(Arrays.asList(user_role)));

    // user1
    user.addRole( user_role );

    // admin2
    admin.addRole( user_role );
    admin.addRole( admin_role );

    // user2
    user2.addRole( user_role );

    // CalendarUser
    calendarUserRepository.save( user );
    calendarUserRepository.save( admin );
    calendarUserRepository.save( user2 );

    List<CalendarUser> users = calendarUserRepository.findAll();

    log.info( "CalendarUsers: {}", users );
  }

} // The End...
