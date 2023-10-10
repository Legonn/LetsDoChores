package com.letschores.demo.configuration;

import com.letschores.demo.model.User;
import com.letschores.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {


    @Bean
    public BCryptPasswordEncoder setEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                        .requestMatchers("/register", "/about","/login").permitAll()
                        .requestMatchers("/player/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/chores/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/review/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/activity/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/activity/createActivityForm").hasRole("ADMIN")
                        .requestMatchers("/statistics").hasRole("ADMIN")
                        .requestMatchers("/statisticsSearch").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(form->form
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll());



        return httpSecurity.build();
    }






}
