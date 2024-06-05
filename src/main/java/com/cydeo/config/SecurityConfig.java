package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        List<UserDetails> userList = new ArrayList<>(Arrays.asList(
                new User("mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))),
                new User("luka", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")))
        ));
        return new InMemoryUserDetailsManager(userList);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(authorizeRequest -> {
                    authorizeRequest.requestMatchers("/user/**").hasAuthority("Admin")
                            .requestMatchers("/project/**").hasAuthority("Manager")
                            .requestMatchers("/task/employee/**").hasAuthority("Employee")
                            .requestMatchers("/task/**").hasAuthority("Manager")
                            .requestMatchers("/", "/login", "/fragments/**", "/assets/**", "/images/**").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .defaultSuccessUrl("/welcome")
                        .failureUrl("/login?error=true")
                        .permitAll())
                .build();

    }


}
