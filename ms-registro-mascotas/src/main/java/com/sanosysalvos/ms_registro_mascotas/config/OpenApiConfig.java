package com.sanosysalvos.ms_registro_mascotas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mascotasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sanos y Salvos - MS Registro de Mascotas")
                        .description("API para el registro, consulta y gestión del estado de las mascotas.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo Sanos y Salvos"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
