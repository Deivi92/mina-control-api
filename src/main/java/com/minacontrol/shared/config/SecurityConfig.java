package com.minacontrol.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Seguridad principal.
 * Delega a configuraciones anidadas basadas en el perfil de Spring activo.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de seguridad para el perfil de desarrollo ('dev').
     * Deshabilita toda la seguridad.
     */
    @Configuration
    @Profile("dev")
    public static class DevSecurityConfig {
        @Bean
        public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }

    /**
     * Configuración de seguridad para todos los perfiles EXCEPTO 'dev' (ej. prod, test).
     * Asegura los endpoints con JWT.
     * 
     * ADVERTENCIA DE SEGURIDAD:
     * Esta configuración requiere implementación adicional para ser funcional en producción:
     * 1. JwtAuthenticationFilter para validar tokens JWT en cada request
     * 2. JwtAuthenticationEntryPoint para manejar errores de autenticación
     * 3. Implementación completa de generación y validación de tokens JWT
     * 4. Configuración de CORS para peticiones cross-origin
     * 
     * Actualmente, esta configuración bloqueará todos los endpoints (excepto /api/auth/**)
     * sin un mecanismo de autenticación JWT funcional.
     */
    @Configuration
    @Profile("!dev")
    public static class DefaultSecurityConfig {
        @Bean
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                // TODO: Agregar .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // TODO: Agregar .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            return http.build();
        }
    }
}