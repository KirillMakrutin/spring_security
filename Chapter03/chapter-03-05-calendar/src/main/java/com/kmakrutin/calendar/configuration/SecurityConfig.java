package com.kmakrutin.calendar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// demonstrates user login requirements for every page in our application,
// provides a login page, authenticates the user, and requires the logged-in user to be
// associated with a role called USER for every URL element

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
  @Autowired
  private AuthenticationProvider authenticationProvider;

  @SuppressWarnings( "deprecation" )
  @Bean
  public static PasswordEncoder passwordEncoder()
  {
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure( AuthenticationManagerBuilder auth )
  {
    auth.authenticationProvider( authenticationProvider );
  }

  // Replaced with the CalendarUserDetailsService
  // Here we exposed InMemoryUserDetailsManager (UserDetailsService) to access the bean
/*  @Bean
  public UserDetailsManager userDetailsService()
  {
    InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

    userDetailsManager.createUser(
        User.withUsername( "user1@example.com" )
            .password( "user1" )
            .roles( "USER" )
            .build() );

    userDetailsManager.createUser(
        User.withUsername( "admin1@example.com" )
            .password( "admin1" )
            .roles( "USER", "ADMIN" )
            .build() );

    return userDetailsManager;
  }*/

  // AuthenticationManagerBuilder object is how Spring Security authenticates the user. In this instance, we utilize an
  // in-memory data store to compare a username and password
/*  @Override
  protected void configure( AuthenticationManagerBuilder auth ) throws Exception
  {
    auth.inMemoryAuthentication()
        .withUser( "user1@example.com" )
        // Prior to Spring Security 5.0 the default PasswordEncoder was NoOpPasswordEncoder which required plain text passwords.
        // In Spring Security 5, the default is DelegatingPasswordEncoder, which required Password Storage Format
        // Add password storage format, for plain text, add {noop}
        .password( "{noop}user1" )
        .roles( "USER" )

        .and()

        .withUser( "admin1@example.com" )
        .password( "{noop}admin1" )
        .roles( "USER", "ADMIN" );

  }*/

  // HttpSecurity object creates a Servlet Filter, which ensures that the currently logged-in user is associated with
  // the appropriate role.
  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http.authorizeRequests()
        .antMatchers( "/css/**", "/webjars/**", "/favicon-*.jpg" ).permitAll()

        .antMatchers( "/" ).access( "hasAnyRole('ANONYMOUS', 'USER')" )
        .antMatchers( "/login/*" ).access( "hasAnyRole('ANONYMOUS', 'USER')" )
        .antMatchers( "/logout/*" ).access( "hasAnyRole('ANONYMOUS', 'USER')" )
        .antMatchers( "/admin/**" ).access( "hasRole('ADMIN' )" )
        .antMatchers( "/events/" ).access( "hasRole('ADMIN' )" )

        .antMatchers( "/signup/*" ).permitAll()

        .antMatchers( "/**" ).access( "hasRole('USER')" )

        .and().formLogin()
        .loginPage( "/login/form" )
        .loginProcessingUrl( "/login" )
        .failureUrl( "/login/form?error" )
        .usernameParameter( "username" )
        .passwordParameter( "password" )
        // without second parameter /default only if user didn't follow to any other protected resource before authentication
        .defaultSuccessUrl( "/default" )
        .permitAll()

        .and().httpBasic()
        .and()
        .logout()
        .logoutUrl( "/logout" )
        .logoutSuccessUrl( "/login/form?logout" )
        // CSRF is enabled by default (will discuss later)
        .and().csrf().disable()
        // for h2
        .headers().frameOptions().disable();
  }
}
