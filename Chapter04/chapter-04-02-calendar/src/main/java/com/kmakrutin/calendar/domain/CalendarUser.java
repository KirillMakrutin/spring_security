package com.kmakrutin.calendar.domain;

import javax.persistence.*;

import lombok.Data;

import java.util.List;

@Entity
@Table( name = "CALENDAR_USERS" )
@Data
public class CalendarUser
{
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  public String getName()
  {
    return firstName + " " + lastName;
  }
}
