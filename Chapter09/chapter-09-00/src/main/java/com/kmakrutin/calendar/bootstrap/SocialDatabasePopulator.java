package com.kmakrutin.calendar.bootstrap;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialDatabasePopulator implements InitializingBean
{
  private final DataSource dataSource;

  @Override
  public void afterPropertiesSet() throws Exception
  {
    ClassPathResource resource = new ClassPathResource( "org/springframework/social/connect/jdbc/JdbcUsersConnectionRepository.sql" );
    executeSql( resource );
  }

  private void executeSql( final Resource resource )
  {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.setContinueOnError( true );
    populator.addScript( resource );
    DatabasePopulatorUtils.execute( populator, dataSource );
  }
}
