package com.udacity.jwdnd.course1.cloudstorage.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SuperDuperDriveSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      // TODO: Remove h2 console configuration from prod
      http.authorizeRequests().antMatchers("/h2/**").permitAll()
              .and().csrf().ignoringAntMatchers("/h2/**")
              .and().headers().frameOptions().sameOrigin();

      http.authorizeRequests()
              .antMatchers("/signup", "/css/**", "/js/**").permitAll()
              .anyRequest().authenticated();

      http.formLogin()
              .loginPage("/login")
              .permitAll()
              .failureUrl("/login-error")
              .and()
              .logout()
              .logoutSuccessUrl("/logout")
              .permitAll();

      http.formLogin()
              .defaultSuccessUrl("/home", true);
      // TODO: check if there are any logout specific configs to be done
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	super.configure(auth);
  }
}
