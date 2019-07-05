package com.kmakrutin.calendar.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table( name = "ROLE" )
public class Role
{
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  private Integer id;

  private String name;

  @ManyToMany( fetch = FetchType.LAZY, mappedBy = "roles" )
  private Set<CalendarUser> users;
}
