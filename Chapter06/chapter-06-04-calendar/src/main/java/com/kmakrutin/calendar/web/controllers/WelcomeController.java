package com.kmakrutin.calendar.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController
{
  @GetMapping( { "", "/" } )
  public String welcome()
  {
    return "index";
  }
}
