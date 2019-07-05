package com.kmakrutin.calendar.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController
{
  @GetMapping( "/default" )
  public String defaultAfterLogin( HttpServletRequest request )
  {
    if ( request.isUserInRole( "ADMIN" ) )
    {
      return "redirect:/events/";
    }

    return "redirect:/";
  }
}
