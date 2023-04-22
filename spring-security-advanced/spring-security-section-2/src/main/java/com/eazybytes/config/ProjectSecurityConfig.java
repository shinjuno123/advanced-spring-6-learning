package com.eazybytes.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact").permitAll()
                .and().formLogin()
                .and().httpBasic()
                .and().build();

        /* Configuration to deny all the requests
        return http.authorizeHttpRequests()
                .anyRequest().denyAll()
                .and().formLogin()
                .and().httpBasic()
                .and().build();

         */

        /* Configuration to permit all the requests
        return http.authorizeHttpRequests().anyRequest().permitAll()
                .and().formLogin()
                .and().httpBasic()
                .and().build();
        */
    }

}
