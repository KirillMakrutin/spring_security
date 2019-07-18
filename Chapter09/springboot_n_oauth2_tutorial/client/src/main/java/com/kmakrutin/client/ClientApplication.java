package com.kmakrutin.client;

import java.security.Principal;
import java.util.Date;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration
@Configuration
@EnableOAuth2Sso
@Controller
public class ClientApplication extends WebSecurityConfigurerAdapter
{
  public static void main( String[] args )
  {
    new SpringApplicationBuilder( ClientApplication.class ).properties( "spring.config.name=client" ).run( args );
  }

  @ResponseBody
  @GetMapping( "/" )
  public String home( Principal user )
  {
    return String.format( "<div>\n"
        + "  <h1>Hello %s</h1>\n"
        + "  <a href=\"logout\">Logout</a>\n"
        + "</div>", user.getName() );
  }

  @ResponseBody
  @GetMapping( "/hello" )
  public String hello()
  {
    return "Hello! " + new Date();
  }

  @GetMapping( "/logout_sso" )
  public String logout()
  {
    return "redirect:https://localhost:8443/sso/";
  }

  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http
        .antMatcher( "/**" )
        .authorizeRequests()
        .antMatchers( "/login**", "/hello" )
        .permitAll()
        .anyRequest().authenticated()

        .and()
        .logout()
        .logoutUrl( "/logout" )
        .logoutSuccessUrl( "/logout_sso" )

        .and().csrf().disable();
  }
}
