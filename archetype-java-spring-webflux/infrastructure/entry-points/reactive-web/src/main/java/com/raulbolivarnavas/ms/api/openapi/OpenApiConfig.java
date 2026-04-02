package com.raulbolivarnavas.ms.api.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${info.app.name:}") String title,
            @Value("${info.app.description:}") String description,
            @Value("${info.app.version:}") String version
    ) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                .name("Raúl Bolívar")
                                .email("raul.bolivar.n@gmail.com"))
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