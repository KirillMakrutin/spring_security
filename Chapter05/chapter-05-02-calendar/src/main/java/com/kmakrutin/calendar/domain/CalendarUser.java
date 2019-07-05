package com.kmakrutin.calendar.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

  @ManyToMany( fetch = FetchType.EAGER )
  @JoinTable( name = "user_role",
      joinColumns = @JoinColumn( name = "user_id" ), inverseJoinColumns = @JoinColumn( name = "role_id" ) )
  private Set<Role> roles;

  public String getName()
  {
    return firstName + " " + lastName;
  }
}
