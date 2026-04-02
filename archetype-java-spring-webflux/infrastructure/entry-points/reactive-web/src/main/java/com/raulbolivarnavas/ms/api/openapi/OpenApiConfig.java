package com.raulbolivarnavas.ms.api.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Mocks Service")
                        .description("""
                                Microservicio de gestión de API Mocks.
                                Permite crear, consultar, actualizar y eliminar definiciones de mocks
                                HTTP, así como recargar la configuración en caliente y cargar datos
                                desde SQL.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Raúl Bolívar")
                                .email("rbolivar@raulbolivarnavas.com"))
                        .license(new License()
                                .name("Internal — Enterprise")
                                .url("https://raulbolivarnavas.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("https://dev.api.raulbolivarnavas.com").description("Dev"),
                        new Server().url("https://qa.api.raulbolivarnavas.com").description("QA")
                ))
                .tags(List.of(
                        new Tag()
                                .name("API Mocks")
                                .description("CRUD de definiciones de mocks HTTP y operaciones de administración")
                ));
    }
}