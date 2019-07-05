package com.kmakrutin.calendar.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document( collection = "calendar_users" )
@Data
public class CalendarUser extends WithIdGenerator implements Persistable<Integer>, Serializable
{
  @Id
  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  private boolean isNew;

  @DBRef( lazy = false )
  private Set<Role> roles = new HashSet<>();

  @PersistenceConstructor
  public CalendarUser( Integer id, String firstName, String lastName, String email, String password )
  {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.isNew = true;
  }

  public String getName()
  {
    return firstName + " " + lastName;
  }

  public void addRole( Role role )
  {
    roles.add( role );
  }
}
