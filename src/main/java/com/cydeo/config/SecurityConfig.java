package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;

    public SecurityConfig(SecurityService securityService) {
        this.securityService = securityService;
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        List<UserDetails> userList = new ArrayList<>(Arrays.asList(
//                new User("mike", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))),
//                new User("luka", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")))
//        ));
//        return new InMemoryUserDetailsManager(userList);
//    }

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
//                        .defaultSuccessUrl("/welcome")
                        .successHandler(new AuthSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login"))
                .rememberMe(rememberMe -> rememberMe.tokenValiditySeconds(120)
                        .key("ticketing")
                        .userDetailsService(securityService)
                )
                .build();

    }


}
