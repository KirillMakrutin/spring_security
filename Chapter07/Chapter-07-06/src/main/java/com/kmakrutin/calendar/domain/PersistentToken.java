package com.kmakrutin.calendar.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table( name = "PERSISTENT_LOGINS" )
@Data
public class PersistentToken
{
  @Column( nullable = false, columnDefinition = "varchar(64)" )
  @Id
  private String series;

  @Column( nullable = false, columnDefinition = "varchar_ignorecase(100)" )
  private String username;

  @Column( nullable = false, columnDefinition = "varchar(64)" )
  private String token;

  @Temporal( TemporalType.TIMESTAMP )
  private Date lastUsed;

}
