package com.kmakrutin.calendar.web.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.domain.Event;
import com.kmakrutin.calendar.services.CalendarService;
import com.kmakrutin.calendar.services.UserContext;
import com.kmakrutin.calendar.web.dto.CreateEventForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping( "/events" )
@RequiredArgsConstructor
public class EventsController
{
  private final CalendarService calendarService;
  private final UserContext userContext;

  @GetMapping( "/" )
  public ModelAndView events()
  {
    return new ModelAndView( "events/list", "events", calendarService.getEvents() );
  }

  @GetMapping( "/my" )
  public ModelAndView myEvents()
  {
    CalendarUser currentUser = userContext.getCurrentUser();
    Integer currentUserId = currentUser.getId();
    ModelAndView result = new ModelAndView( "events/my", "events", calendarService.findForUser( currentUserId ) );
    result.addObject( "currentUser", currentUser );
    return result;
  }

  @GetMapping( "/{eventId}" )
  public ModelAndView show( @PathVariable int eventId )
  {
    Event event = calendarService.getEvent( eventId );
    return new ModelAndView( "events/show", "event", event );
  }

  @GetMapping( "/form" )
  public String createEventForm( @ModelAttribute CreateEventForm createEventForm )
  {
    return "events/create";
  }

  /**
   * Populates the form for creating an event with valid information. Useful so that users do not have to think when
   * filling out the form for testing.
   */
  @PostMapping( value = "/new", params = "auto" )
  public String createEventFormAutoPopulate( @ModelAttribute CreateEventForm createEventForm )
  {
    // provide default values to make user submission easier
    createEventForm.setSummary( "A new event...." );
    createEventForm.setDescription( "This was autopopulated to save time creating a valid event." );
    createEventForm.setWhen( new Date() );

    // make the attendee not the current user
    createEventForm.setAttendeeEmail( userContext.getCurrentUser().getEmail() );

    return "events/create";
  }

  @PostMapping( value = "/new" )
  public String createEvent( @Valid CreateEventForm createEventForm, BindingResult result,
      RedirectAttributes redirectAttributes )
  {
    if ( result.hasErrors() )
    {
      return "events/create";
    }
    CalendarUser attendee = calendarService.findUserByEmail( createEventForm.getAttendeeEmail() );
    if ( attendee == null )
    {
      result.rejectValue( "attendeeEmail", "attendeeEmail.missing",
          "Could not find a user for the provided Attendee Email" );
    }
    if ( result.hasErrors() )
    {
      return "events/create";
    }
    Event event = new Event(
        Event.nextId(),
        createEventForm.getSummary(),
        createEventForm.getDescription(),
        createEventForm.getWhen(),
        userContext.getCurrentUser(),
        attendee
    );

    event.setNew( true );
    calendarService.createEvent( event );

    redirectAttributes.addFlashAttribute( "message", "Successfully added the new event" );

    return "redirect:/events/my";
  }
}
