package com.kmakrutin.springboot_n_oauth2_tutorial.config;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.filter.CompositeFilter;

import com.kmakrutin.springboot_n_oauth2_tutorial.authentication.OAuth2ClientAuthenticationProcessingAndSaveFilter;
import com.kmakrutin.springboot_n_oauth2_tutorial.service.OAuthUserService;

@EnableOAuth2Client
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  @Autowired
  private OAuth2ClientContext oauth2ClientContext;

  @Autowired
  private OAuthUserService userService;

  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http
        .antMatcher( "/**" )
        .authorizeRequests()

        .antMatchers( "/", "/js/**", "/login**", "/webjars/**", "/error**", "/h2-console/**" )
        .permitAll()

        .anyRequest()
        .authenticated()

        .and()
        .logout()
        .logoutSuccessUrl( "/" )
        .permitAll()

        .and()
        .csrf()
        .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() )
        .ignoringAntMatchers( "/h2-console/**" )

        .and()
        .addFilterAt( ssoFilter(), BasicAuthenticationFilter.class )

        // for h2
        .headers()
        .frameOptions()
        .disable();

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
    List<Filter> filters = new ArrayList<>( 2 );

    OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingAndSaveFilter( userService, "/login/facebook" );
    OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate( facebook(), oauth2ClientContext );
    facebookFilter.setRestTemplate( facebookTemplate );
    UserInfoTokenServices tokenServices = new UserInfoTokenServices( facebookResource().getUserInfoUri(), facebook().getClientId() );
    tokenServices.setRestTemplate( facebookTemplate );
    facebookFilter.setTokenServices( tokenServices );
    filters.add( facebookFilter );

    OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingAndSaveFilter( userService, "/login/github" );
    OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate( github(), oauth2ClientContext );
    githubFilter.setRestTemplate( githubTemplate );
    tokenServices = new UserInfoTokenServices( githubResource().getUserInfoUri(), github().getClientId() );
    tokenServices.setRestTemplate( githubTemplate );
    githubFilter.setTokenServices( tokenServices );
    filters.add( githubFilter );

    filter.setFilters( filters );

    return filter;
  }

  @Bean
  @ConfigurationProperties( "github.client" )
  public AuthorizationCodeResourceDetails github()
  {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  @ConfigurationProperties( "github.resource" )
  public ResourceServerProperties githubResource()
  {
    return new ResourceServerProperties();
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
