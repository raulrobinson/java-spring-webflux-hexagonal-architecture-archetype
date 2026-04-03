package com.raulbolivarnavas.ms.parameterstore.adapter;

import com.raulbolivarnavas.ms.ports.out.ParameterStoreGateway;
import io.github.raulbolivar.aws.parameter.port.ParameterStorePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParameterStoreAdapterImpl implements ParameterStoreGateway {

    private final ParameterStorePort ssm;

    @Override
    public Mono<String> getParameter(String name) {
        return Mono.fromCallable(() -> ssm.getParameterValue(name).orElseThrow())
                .doOnSuccess(v -> log.info("[ADAPTER] Retrieved parameter '{}'", name))
                .doOnError(e -> log.error("[ADAPTER] Error getting parameter '{}': {}", name, e.getMessage()));
    }
}
