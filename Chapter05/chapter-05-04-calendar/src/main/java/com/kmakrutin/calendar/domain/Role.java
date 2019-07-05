package com.kmakrutin.calendar.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document( collection = "role" )
@Data
public class Role extends WithIdGenerator implements Persistable<Integer>, Serializable
{
  @Id
  private Integer id;

  private String name;

  private boolean isNew;

  @PersistenceConstructor
  public Role( Integer id, String name )
  {
    this.id = id;
    this.name = name;
    this.isNew = true;
  }
}
