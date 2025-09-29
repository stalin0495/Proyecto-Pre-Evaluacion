package com.pichincha.customer.infrastructure.input.adapter.rest.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // deshabilita CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // TODO abierto
                );
        return http.build();
    }
}


