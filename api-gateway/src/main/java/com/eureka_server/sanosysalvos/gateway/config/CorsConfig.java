package com.eureka_server.sanosysalvos.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Única fuente de configuración CORS de todo el ecosistema: los microservicios
 * individuales ya NO declaran su propio @CrossOrigin (se quitó a propósito)
 * para evitar que el navegador reciba el header Access-Control-Allow-Origin
 * duplicado ("*, *") cuando la petición pasa por este Gateway.
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
