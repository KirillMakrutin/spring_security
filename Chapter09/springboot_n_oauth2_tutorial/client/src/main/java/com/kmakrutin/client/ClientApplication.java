package com.kmakrutin.client;

import java.security.Principal;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@Configuration
@EnableOAuth2Sso
@RestController
public class ClientApplication extends WebSecurityConfigurerAdapter
{
  public static void main( String[] args )
  {
    new SpringApplicationBuilder( ClientApplication.class ).properties( "spring.config.name=client" ).run( args );
  }

  @GetMapping( "/" )
  public String home( Principal user )
  {
    return "Hello " + user.getName();
  }

  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http
        .antMatcher( "/**" )
        .authorizeRequests()
        .antMatchers( "/login**" )
        .permitAll()
        .anyRequest().authenticated();
  }
}
