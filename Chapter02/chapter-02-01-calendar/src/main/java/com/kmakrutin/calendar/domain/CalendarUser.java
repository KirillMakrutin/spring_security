package com.kmakrutin.chapter0100calendar.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

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
