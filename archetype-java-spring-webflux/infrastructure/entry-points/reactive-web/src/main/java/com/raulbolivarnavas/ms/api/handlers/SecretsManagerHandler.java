package com.raulbolivarnavas.ms.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raulbolivarnavas.ms.ports.in.ISecretsManagerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecretsManagerHandler {

    private final ISecretsManagerUseCase secretsManagerUseCase;
    private final ObjectMapper objectMapper;

    public Mono<ServerResponse> getSecret(ServerRequest request) {
        String name = request.queryParam("name").orElseThrow(() ->
                new IllegalArgumentException("Missing 'name' query secret"));

        return secretsManagerUseCase.getSecretAsMap(name)
                .flatMap(value -> {
                    try {
                        // value ya es un JSON string → deserializar y devolver como objeto
                        Object parsed = objectMapper.readValue(value.toString(), Object.class);
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(parsed);
                    } catch (Exception e) {
                        // Si no es JSON válido, devolver como string plano en un wrapper
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("value", value));
                    }
                })
                .onErrorResume(e -> ServerResponse.status(500)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("error", e.getMessage())));
    }
}
