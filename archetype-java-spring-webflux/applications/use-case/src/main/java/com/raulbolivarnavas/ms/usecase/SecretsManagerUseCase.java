package com.raulbolivarnavas.ms.usecase;

import com.raulbolivarnavas.ms.ports.in.ISecretsManagerUseCase;
import com.raulbolivarnavas.ms.ports.out.SecretsManagerGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecretsManagerUseCase implements ISecretsManagerUseCase {

    private final SecretsManagerGateway secretsManagerGateway;

    @Override
    public Mono<Map<String, String>> getSecretAsMap(String name) {
        return secretsManagerGateway.getSecretAsMap(name)
                .doOnSuccess(s -> log.info("[USE CASE] Retrieved secret '{}' as map", name))
                .doOnError(e -> log.error("[USE CASE] Error getting secret '{}' as map: {}", name, e.getMessage()));
    }
}
