package com.kmakrutin.calendar.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport
{
  @Override
  protected void addViewControllers( ViewControllerRegistry registry )
  {
    super.addViewControllers( registry );

    registry.addViewController( "/login/form" ).setViewName( "login" );
    registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
  }
}
