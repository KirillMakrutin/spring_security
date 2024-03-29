package com.kmakrutin.springboot_n_oauth2_tutorial.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableOAuth2Sso
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http
        .antMatcher( "/**" )
        .authorizeRequests()

        .antMatchers( "/", "/login**", "/webjars/**", "/error**" )
        .permitAll()

        .anyRequest()
        .authenticated()

        .and().logout().logoutSuccessUrl( "/" ).permitAll()
        .and().csrf().csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() );
  }
}
