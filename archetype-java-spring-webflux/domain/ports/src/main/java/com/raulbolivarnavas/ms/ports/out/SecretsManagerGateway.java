package com.raulbolivarnavas.ms.ports.out;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface SecretsManagerGateway {
    Mono<Map<String, String>> getSecretAsMap(String name);
}
