package com.minacontrol.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Seguridad para el perfil de 'desarrollo' (dev).
 * Esta configuración deshabilita la seguridad, permitiendo el acceso a todos los endpoints
 * sin autenticación.
 * <p>
 * <strong>ATENCIÓN:</strong> Esta configuración solo debe usarse para desarrollo y pruebas locales.
 * Nunca debe estar activa en un entorno de producción.
 * </p>
 */
@Configuration
@Profile("dev")
public class DevelopmentSecurityConfig {

    /**
     * Define un bean de PasswordEncoder para que esté disponible en el contexto de la aplicación,
     * incluso en el perfil de desarrollo. Esto es necesario porque los servicios de negocio
     * (como ServicioAutenticacionImpl) dependen de él para funcionar correctamente.
     *
     * @return una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define un SecurityFilterChain que permite todas las solicitudes HTTP sin autenticación
     * y deshabilita la protección CSRF.
     *
     * @param http el objeto HttpSecurity para configurar.
     * @return el SecurityFilterChain configurado.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
