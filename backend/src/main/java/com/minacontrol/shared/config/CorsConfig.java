package com.minacontrol.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para permitir solicitudes desde el frontend.
 * Esta configuración es especialmente importante para el entorno de desarrollo y pruebas.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir solicitudes CORS desde el frontend Vite (puerto 5173) y el preview (puerto 4173)
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:4173", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}