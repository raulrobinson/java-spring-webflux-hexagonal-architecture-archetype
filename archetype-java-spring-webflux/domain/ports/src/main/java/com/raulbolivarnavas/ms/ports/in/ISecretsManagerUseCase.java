package com.raulbolivarnavas.ms.ports.in;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface ISecretsManagerUseCase {
    Mono<Map<String, String>> getSecretAsMap(String name);
}
