package com.kmakrutin.springboot_n_oauth2_tutorial.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

import com.kmakrutin.springboot_n_oauth2_tutorial.authentication.OAuth2ClientResources;

@EnableOAuth2Client
@EnableAuthorizationServer
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  @Autowired
  private OAuth2ClientContext oauth2ClientContext;

  @Bean
  public PasswordEncoder passwordEncoder()
  {
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure( final AuthenticationManagerBuilder auth ) throws Exception
  {
    auth.inMemoryAuthentication()
        .withUser( "user1" ).password( passwordEncoder().encode( "user1Pass" ) ).roles( "USER" )
        .and()
        .withUser( "user2" ).password( passwordEncoder().encode( "user2Pass" ) ).roles( "USER" )
        .and()
        .withUser( "admin" ).password( passwordEncoder().encode( "adminPass" ) ).roles( "ADMIN" );
  }

  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http
        .antMatcher( "/**" )
        .authorizeRequests()

        .antMatchers( "/", "/js/**", "/login**", "/basic_login**", "/webjars/**", "/error**" )
        .permitAll()

        .anyRequest()
        .authenticated()

        .and()
        .formLogin()
        .loginProcessingUrl( "/basic_login" )
        .usernameParameter( "username" )
        .passwordParameter( "password" )
        .defaultSuccessUrl( "/", false )

        .and().exceptionHandling()
        .authenticationEntryPoint( new LoginUrlAuthenticationEntryPoint( "/" ) )

        .and()
        .logout()
        .logoutSuccessUrl( "/" )
        .permitAll()

        .and()
        .csrf()
        .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() )
        .ignoringAntMatchers( "/basic_login**" )

        .and()
        .addFilterAt( ssoFilter(), BasicAuthenticationFilter.class );

  }

  /**
   * this filter is created in new method where we use the OAuth2ClientContext
   *
   * The main change on the server is to add an additional security filter to handle the "/login/github" requests coming
   * from our new link. We already have a custom authentication filter for Facebook created in our ssoFilter() method,
   * so all we need to do is replace that with a composite that can handle more than one authentication path
   */
  private Filter ssoFilter()
  {
    CompositeFilter filter = new CompositeFilter();
    List<Filter> filters = new ArrayList<>();
    filters.add( ssoFilter( facebook(), "/login/facebook" ) );
    filters.add( ssoFilter( github(), "/login/github" ) );
    filter.setFilters( filters );

    return filter;
  }

  private Filter ssoFilter( OAuth2ClientResources client, String path )
  {
    OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter( path );
    OAuth2RestTemplate template = new OAuth2RestTemplate( client.getClient(), oauth2ClientContext );
    filter.setRestTemplate( template );
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(
        client.getResource().getUserInfoUri(),
        client.getClient().getClientId() );
    tokenServices.setRestTemplate( template );
    filter.setTokenServices( tokenServices );

    return filter;
  }

  @Bean
  @ConfigurationProperties( "github" )
  public OAuth2ClientResources github()
  {
    return new OAuth2ClientResources();
  }

  @Bean
  @ConfigurationProperties( "facebook" )
  public OAuth2ClientResources facebook()
  {
    return new OAuth2ClientResources();
  }
}
