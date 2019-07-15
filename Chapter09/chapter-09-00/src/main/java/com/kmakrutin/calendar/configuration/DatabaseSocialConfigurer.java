package com.kmakrutin.calendar.configuration;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DatabaseSocialConfigurer extends SocialConfigurerAdapter
{
  private final DataSource dataSource;

  @Override
  public UsersConnectionRepository getUsersConnectionRepository( ConnectionFactoryLocator connectionFactoryLocator )
  {
    TextEncryptor textEncryptor = Encryptors.noOpText();
    return new JdbcUsersConnectionRepository( dataSource, connectionFactoryLocator, textEncryptor );
  }

  @Override
  public void addConnectionFactories( ConnectionFactoryConfigurer config, Environment env )
  {
    super.addConnectionFactories( config, env );
  }
}
