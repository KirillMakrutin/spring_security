package com.kmakrutin.calendar.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// demonstrates user login requirements for every page in our application,
// provides a login page, authenticates the user, and requires the logged-in user to be
// associated with a role called USER for every URL element

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

  // AuthenticationManagerBuilder object is how Spring Security authenticates the user. In this instance, we utilize an
  // in-memory data store to compare a username and password
  @Override
  protected void configure( AuthenticationManagerBuilder auth ) throws Exception
  {
    auth.inMemoryAuthentication()
        .withUser( "user1@exmaple.com" )
        // Prior to Spring Security 5.0 the default PasswordEncoder was NoOpPasswordEncoder which required plain text passwords.
        // In Spring Security 5, the default is DelegatingPasswordEncoder, which required Password Storage Format
        // Add password storage format, for plain text, add {noop}
        .password( "{noop}user1" )
        .roles( "USER" );

  }

  // HttpSecurity object creates a Servlet Filter, which ensures that the currently logged-in user is associated with
  // the appropriate role.
  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http.authorizeRequests()
        .antMatchers( "/**" ).access( "hasRole('USER')" )
        // equivalent to <http auto-config="true">
        .and().formLogin()
        .and().httpBasic()
        .and().logout()
        // CSRF is enabled by default (will discuss later)
        .and().csrf().disable();
  }
}
