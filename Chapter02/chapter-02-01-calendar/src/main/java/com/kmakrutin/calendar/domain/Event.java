package com.kmakrutin.chapter0100calendar.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table( name = "EVENTS" )
@Data
public class Event
{
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @NotEmpty( message = "Summary is required" )
  private String summary;

  @NotEmpty( message = "Description is required" )
  private String description;

  @NotNull( message = "When is required" )
  @Temporal( TemporalType.TIMESTAMP )
  private Date when;

  @ManyToOne
  @JoinColumn( name = "owner" )
  private CalendarUser owner;

  @ManyToOne
  @JoinColumn( name = "attendee" )
  private CalendarUser attendee;
}
