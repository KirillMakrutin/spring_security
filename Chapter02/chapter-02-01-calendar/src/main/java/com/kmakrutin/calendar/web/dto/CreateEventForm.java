package com.kmakrutin.chapter0100calendar.web.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventForm
{
  @NotEmpty( message = "Attendee Email is required" )
  @Email( message = "Attendee Email must be a valid email" )
  private String attendeeEmail;
  @NotEmpty( message = "Summary is required" )
  private String summary;
  @NotEmpty( message = "Description is required" )
  private String description;
  @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm" )
  @NotNull( message = "Event Date/Time is required" )
  private Date when;
}
