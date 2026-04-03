package com.raulbolivarnavas.ms.secretsmanager.adapter;

import com.raulbolivarnavas.ms.ports.out.SecretsManagerGateway;
import io.github.raulbolivar.aws.secrets.port.SecretsManagerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecretsManagerAdapterImpl implements SecretsManagerGateway {

    private final SecretsManagerPort secrets;

    @Override
    public Mono<Map<String, String>> getSecretAsMap(String name) {
        return Mono.fromCallable(() -> secrets.getSecretAsMap(name))
                .doOnSuccess(v -> log.info("[ADAPTER] Retrieved secret '{}'", name))
                .doOnError(e -> log.error("[ADAPTER] Error getting secret '{}': {}", name, e.getMessage()));
    }
}
