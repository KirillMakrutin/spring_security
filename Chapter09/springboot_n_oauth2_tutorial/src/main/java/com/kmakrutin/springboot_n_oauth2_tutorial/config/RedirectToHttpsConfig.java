package com.kmakrutin.springboot_n_oauth2_tutorial.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Not necessary, but in some cases it might be a good idea to make your application accessible over HTTP too, but redirect
 * all traffic to HTTPS. To achieve this we’ll need to add a second Tomcat connector, but currently it is not possible
 * to configure two connector in the application.properties like mentioned before.
 * Because of this we’ll add the HTTP connector programmatically and make sure it redirects all traffic to our HTTPS connector.
 *
 * For this we will need to add the TomcatEmbeddedServletContainerFactory bean
 */
@Configuration
public class RedirectToHttpsConfig
{
  @Bean
  public ServletWebServerFactory servletContainer()
  {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory()
    {
      @Override
      protected void postProcessContext( Context context )
      {
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint( "CONFIDENTIAL" );
        SecurityCollection collection = new SecurityCollection();
        collection.addPattern( "/*" );
        securityConstraint.addCollection( collection );
        context.addConstraint( securityConstraint );
      }
    };
    tomcat.addAdditionalTomcatConnectors( redirectConnector() );
    return tomcat;
  }

  private Connector redirectConnector()
  {
    Connector connector = new Connector( TomcatServletWebServerFactory.DEFAULT_PROTOCOL );
    connector.setScheme( "http" );
    connector.setPort( 8080 );
    connector.setSecure( false );
    connector.setRedirectPort( 8443 );
    return connector;
  }
}
