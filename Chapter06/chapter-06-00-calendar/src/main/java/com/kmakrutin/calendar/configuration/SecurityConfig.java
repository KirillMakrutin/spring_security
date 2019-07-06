package com.kmakrutin.calendar.configuration;

import com.kmakrutin.calendar.authentication.DomainUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// demonstrates user login requirements for every page in our application,
// provides a login page, authenticates the user, and requires the logged-in user to be
// associated with a role called USER for every URL element

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userSearchBase("")
                .userSearchFilter("(uid={0})")

                .groupSearchBase("ou=Groups")
                .groupSearchFilter("(uniqueMember={0})")

                //.contextSource(contextSource())
                .contextSource()
                .url("ldap://localhost:33389/dc=jbcpcalendar,dc=com")
                .and()
                .passwordCompare()
                .passwordEncoder(passwordEncoder())
                .passwordAttribute("userPassword");

    }

/*    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(
                Arrays.asList("ldap://localhost:33389/"), "dc=jbcpcalendar,dc=com");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/webjars/**", "/favicon-*.jpg").permitAll()

                .antMatchers("/").access("hasAnyRole('ANONYMOUS', 'USER')")
                .antMatchers("/logout/*").access("hasRole('USER')")
                .antMatchers("/admin/**").access("hasRole('ADMIN' )")
                .antMatchers("/events/").access("hasRole('ADMIN' )")

                .antMatchers("/login/*").permitAll()
                .antMatchers("/signup/*").permitAll()

                .antMatchers("/**").access("hasRole('USER')")

                .and().exceptionHandling()
                .accessDeniedPage("/errors/403")
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())

                .and().httpBasic()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/form?logout")
                // CSRF is enabled by default (will discuss later)
                .and().csrf().disable()
                // for h2
                .headers().frameOptions().disable();

        // add custom AuthenticationFilter
        http.addFilterAt(domainUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public DomainUsernamePasswordAuthenticationFilter domainUsernamePasswordAuthenticationFilter() throws Exception {
        DomainUsernamePasswordAuthenticationFilter filter = new DomainUsernamePasswordAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/login");
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");

        filter.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler() {{
            setDefaultTargetUrl("/default");
        }});
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {{
            setDefaultFailureUrl("/login/form?error");
        }});
        filter.afterPropertiesSet();

        return filter;
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login/form");
    }
}
