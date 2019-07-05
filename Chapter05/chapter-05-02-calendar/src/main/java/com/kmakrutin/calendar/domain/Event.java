package com.kmakrutin.calendar.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne( fetch = FetchType.LAZY )
  @JoinColumn( name = "owner", referencedColumnName = "id" )
  private CalendarUser owner;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne( fetch = FetchType.LAZY )
  @JoinColumn( name = "attendee", referencedColumnName = "id" )
  private CalendarUser attendee;
}
