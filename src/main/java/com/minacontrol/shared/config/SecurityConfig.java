package com.minacontrol.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Añadido para habilitar la seguridad a nivel de método
@Profile("!dev")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF porque usamos una API sin estado (stateless) con JWT.
            .csrf(csrf -> csrf.disable())
            // Configurar las reglas de autorización para cada endpoint.
            .authorizeHttpRequests(auth -> auth
                // Permitir el acceso público a todos los endpoints bajo /api/auth/.
                .requestMatchers("/api/auth/**").permitAll()
                // Cualquier otra petición debe ser autenticada.
                .anyRequest().authenticated()
            )
            // Configurar la gestión de sesiones como STATELESS.
            // Spring Security no creará ni utilizará HttpSession.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
