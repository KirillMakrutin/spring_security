package com.kmakrutin.calendar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kmakrutin.calendar.authentication.DomainUsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

// demonstrates user login requirements for every page in our application,
// provides a login page, authenticates the user, and requires the logged-in user to be
// associated with a role called USER for every URL element

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
  private static final String GROUP_AUTHORITIES_BY_USERNAME_QUERY = "select g.id, g.group_name, ga.authority " +
          " from groups g, group_members gm, group_authorities ga" +
          " where gm.username = ? and g.id = ga.group_id and g.id = gm.group_id";
  private static final String CUSTOM_USERS_BY_USERNAME_QUERY = "select email, password, true from calendar_users where email = ?";
  private static final String CUSTOM_AUTHORITIES_BY_USERNAME_QUERY = "select cua.id, cua.authority from calendar_user_authorities cua join calendar_users cu ON cu.id = cua.calendar_user where cu.email = ?";


  @Autowired
  private DataSource dataSource;

  @SuppressWarnings( "deprecation" )
  @Bean
  public static PasswordEncoder passwordEncoder()
  {
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
            .dataSource( dataSource )
            .usersByUsernameQuery(CUSTOM_USERS_BY_USERNAME_QUERY)
            .authoritiesByUsernameQuery(CUSTOM_AUTHORITIES_BY_USERNAME_QUERY);
            // .groupAuthoritiesByUsername( GROUP_AUTHORITIES_BY_USERNAME_QUERY );
  }

/*  @Bean
  protected UserDetailsService userDetailsService(DataSource dataSource) {
    JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
    manager.setDataSource(dataSource);
    return manager;
  }*/

  // HttpSecurity object creates a Servlet Filter, which ensures that the currently logged-in user is associated with
  // the appropriate role.
  @Override
  protected void configure( HttpSecurity http ) throws Exception
  {
    http.authorizeRequests()
        .antMatchers( "/css/**", "/webjars/**", "/favicon-*.jpg" ).permitAll()

        .antMatchers( "/" ).access( "hasAnyRole('ANONYMOUS', 'USER')" )
        .antMatchers( "/logout/*" ).access( "hasRole('USER')" )
        .antMatchers( "/admin/**" ).access( "hasRole('ADMIN' )" )
        .antMatchers( "/events/" ).access( "hasRole('ADMIN' )" )

        .antMatchers( "/login/*" ).permitAll()
        .antMatchers( "/signup/*" ).permitAll()

        .antMatchers( "/**" ).access( "hasRole('USER')" )

        .and().exceptionHandling()
        .accessDeniedPage( "/errors/403" )
        .authenticationEntryPoint( loginUrlAuthenticationEntryPoint() )

        .and().httpBasic()
        .and()
        .logout()
        .logoutUrl( "/logout" )
        .logoutSuccessUrl( "/login/form?logout" )
        // CSRF is enabled by default (will discuss later)
        .and().csrf().disable()
        // for h2
        .headers().frameOptions().disable();

    // add custom AuthenticationFilter
    http.addFilterAt( domainUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );
  }

  @Bean
  public DomainUsernamePasswordAuthenticationFilter domainUsernamePasswordAuthenticationFilter() throws Exception
  {
    DomainUsernamePasswordAuthenticationFilter filter = new DomainUsernamePasswordAuthenticationFilter( authenticationManager() );
    filter.setFilterProcessesUrl( "/login" );
    filter.setUsernameParameter( "username" );
    filter.setPasswordParameter( "password" );

    filter.setAuthenticationSuccessHandler( new SavedRequestAwareAuthenticationSuccessHandler()
    {{
      setDefaultTargetUrl( "/default" );
    }} );
    filter.setAuthenticationFailureHandler( new SimpleUrlAuthenticationFailureHandler()
    {{
      setDefaultFailureUrl( "/login/form?error" );
    }} );
    filter.afterPropertiesSet();

    return filter;
  }

  @Bean
  public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint()
  {
    return new LoginUrlAuthenticationEntryPoint( "/login/form" );
  }
}
