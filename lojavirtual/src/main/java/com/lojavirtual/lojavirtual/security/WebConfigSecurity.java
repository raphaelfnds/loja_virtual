package com.lojavirtual.lojavirtual.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.GET, "/salvarAcesso").permitAll() // Permite GET no endpoint
                .requestMatchers(HttpMethod.POST, "/salvarAcesso").permitAll() // Permite POST no endpoint
                .requestMatchers(HttpMethod.GET, "/deleteAcesso").permitAll() // Permite GET no endpoint
                .requestMatchers(HttpMethod.POST, "/deleteAcesso").permitAll() // Permite POST no endpoint
                .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição
            );

        return http.build();
    }
}