package com.kmakrutin.calendar.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.kmakrutin.calendar.authentication.ProviderConnectionSignUp;
import com.kmakrutin.calendar.authentication.SocialAuthenticationUtils;

@Configuration
public class SocialConfig
{
  @Value( "${spring.social.twitter.appId}" )
  private String consumerKey;
  @Value( "${spring.social.twitter.appSecret}" )
  private String consumerSecret;

  @Autowired
  private ProviderConnectionSignUp providerConnectionSignup;

  @Autowired
  private DataSource dataSource;

  @Bean
  ConnectionFactoryLocator connectionFactoryLocator()
  {
    ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
    registry.addConnectionFactory( new TwitterConnectionFactory( consumerKey, consumerSecret ) );
    return registry;
  }

  /**
   * Defines callback methods to customize Java-based configuration enabled by {@code @EnableWebMvc}.
   * {@code @EnableWebMvc}-annotated classes may implement this interface or
   * extend {@link SocialConfigurerAdapter}, which provides some default configuration.
   * In this initialization we are configuring the {@link SocialConfigurer} to use our local
   * database to store OAuth provider details for a given user.
   */
  @Bean
  public SocialConfigurer socialConfigurerAdapter( final DataSource dataSource )
  {
    // https://github.com/spring-projects/spring-social/blob/master/spring-social-config/src/main/java/org/springframework/social/config/annotation/SocialConfiguration.java#L87
    return new DatabaseSocialConfigurer( dataSource );
  }

  /**
   * create a custom authenticate utility method to create an
   * Authentication objet and add it to our SecurityContext based on
   * the returned provider details.
   *
   * @return
   */
  @Bean
  public SignInAdapter authSignInAdapter()
  {
    return ( userId, connection, request ) -> {
      SocialAuthenticationUtils.authenticate( connection );
      return null;
    };
  }

  @Bean
  public UsersConnectionRepository usersConnectionRepository()
  {
    JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository( dataSource, connectionFactoryLocator(),
        Encryptors.noOpText() );
    repository.setConnectionSignUp( providerConnectionSignup );
    return repository;
  }

  /**
   * Configuring a {@link ProviderSignInController} to intercept OAuth2
   * requests that will be used to initiate an OAuth2 handshake with the specified
   * OAuth2 provider.
   *
   * @return
   */
  @Bean
  public ProviderSignInController providerSignInController()
  {
    return new ProviderSignInController( connectionFactoryLocator(), usersConnectionRepository(), authSignInAdapter() );
  }
}
