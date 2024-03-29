package com.kmakrutin.calendar.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class SignupForm
{
  @NotEmpty( message = "First Name is required" )
  private String firstName;
  @NotEmpty( message = "Last Name is required" )
  private String lastName;
  @Email( message = "Please provide a valid email address" )
  @NotEmpty( message = "Email is required" )
  private String email;
  @NotEmpty( message = "Password is required" )
  private String password;

}
