package com.sanosysalvos.bff.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate balanceado: permite llamar a los microservicios usando su
     * spring.application.name (ej: http://ms-registro-mascotas/api/mascotas/listar)
     * en lugar de host:puerto fijos. Spring Cloud LoadBalancer resuelve la
     * instancia real a través de Eureka.
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
