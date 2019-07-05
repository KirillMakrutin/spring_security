package com.kmakrutin.calendar.web.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kmakrutin.calendar.domain.CalendarUser;
import com.kmakrutin.calendar.services.CalendarService;
import com.kmakrutin.calendar.services.UserContext;
import com.kmakrutin.calendar.web.dto.SignupForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController
{
  private final CalendarService calendarService;
  private final UserContext userContext;

  @GetMapping( "/signup/form" )
  public String signupForm( @ModelAttribute SignupForm signupForm )
  {
    return "signup/form";
  }

  @PostMapping( "/signup/new" )
  public String signup( @ModelAttribute( "signupForm" ) @Valid SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes )
  {
    if ( result.hasErrors() )
    {
      return "signup/form";
    }

    String email = signupForm.getEmail().trim();
    if ( calendarService.findUserByEmail( email ) != null )
    {
      result.rejectValue( "email", "errors.signup.email", "Email address is already in use." );
      return "signup/form";
    }

    CalendarUser calendarUser = new CalendarUser(
        CalendarUser.nextId(),
        signupForm.getFirstName(),
        signupForm.getLastName(),
        email,
        signupForm.getPassword()
    );

    calendarUser.setNew( true );

    userContext.setCurrentUser( calendarService.createUser( calendarUser ) );

    redirectAttributes.addFlashAttribute( "message", "Success" );
    return "redirect:/";
  }
}
