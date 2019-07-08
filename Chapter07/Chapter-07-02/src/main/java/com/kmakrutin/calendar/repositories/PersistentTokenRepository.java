package com.kmakrutin.calendar.repositories;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class PersistentTokenRepository extends JdbcTokenRepositoryImpl
{
  private final DataSource dataSource;

  @PostConstruct
  public void init()
  {
    setDataSource( dataSource );
  }
}
