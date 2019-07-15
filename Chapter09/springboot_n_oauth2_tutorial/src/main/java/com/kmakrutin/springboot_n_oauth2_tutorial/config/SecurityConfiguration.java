package com.kmakrutin.springboot_n_oauth2_tutorial.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableOAuth2Client
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  @Autowired
  private OAuth2ClientContext oauth2ClientContext;

  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http
        .antMatcher( "/**" )
        .authorizeRequests()

        .antMatchers( "/", "/js/**", "/login**", "/webjars/**", "/error**" )
        .permitAll()

        .anyRequest()
        .authenticated()

        .and().logout().logoutSuccessUrl( "/" ).permitAll()

        .and().csrf().csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() )

        .and().addFilterAt( ssoFilter(), BasicAuthenticationFilter.class );

  }

  /**
   * this filter is created in new method where we use the OAuth2ClientContext
   */
  private Filter ssoFilter()
  {
    OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter( "/login/facebook" );

    OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate( facebook(), oauth2ClientContext );
    facebookFilter.setRestTemplate( facebookTemplate );

    UserInfoTokenServices tokenServices = new UserInfoTokenServices( facebookResource().getUserInfoUri(), facebook().getClientId() );
    tokenServices.setRestTemplate( facebookTemplate );
    facebookFilter.setTokenServices( tokenServices );

    return facebookFilter;
  }

  /**
   * the filter also needs to know about the client registration with Facebook:
   */
  @Bean
  @ConfigurationProperties( "facebook.client" )
  public AuthorizationCodeResourceDetails facebook()
  {
    return new AuthorizationCodeResourceDetails();
  }

  /**
   * and to complete the authentication it needs to know where the user info endpoint is in Facebook
   */
  @Bean
  @ConfigurationProperties( "facebook.resource" )
  public ResourceServerProperties facebookResource()
  {
    return new ResourceServerProperties();
  }

  /**
   * The last change we need to make is to explicitly support the redirects from our app to Facebook.
   * This is handled in Spring OAuth2 with a servlet Filter, and the filter is already available in the application context
   * because we used @EnableOAuth2Client. All that is needed is to wire the filter up so that it gets called in the right
   * order in our Spring Boot application. To do that we need a FilterRegistrationBean.
   *
   * We autowire the already available filter, and register it with a sufficiently low order that it comes before the main
   * Spring Security filter. In this way we can use it to handle redirects signaled by exceptions in authentication requests
   */
  @Bean
  public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration( OAuth2ClientContextFilter filter )
  {
    FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter( filter );
    registration.setOrder( -100 );
    return registration;
  }
}
