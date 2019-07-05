package com.kmakrutin.calendar.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Document( collection = "events" )
@Data
public class Event extends WithIdGenerator implements Persistable<Integer>, Serializable
{
  @Id
  private Integer id;
  private String summary;
  private String description;
  private Date when;
  private boolean isNew;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @DBRef
  private CalendarUser owner;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @DBRef
  private CalendarUser attendee;

  @PersistenceConstructor
  public Event( Integer id, String summary, String description, Date when, CalendarUser owner, CalendarUser attendee )
  {
    this.id = id;
    this.summary = summary;
    this.description = description;
    this.when = when;
    this.owner = owner;
    this.attendee = attendee;
    this.isNew = true;
  }
}
