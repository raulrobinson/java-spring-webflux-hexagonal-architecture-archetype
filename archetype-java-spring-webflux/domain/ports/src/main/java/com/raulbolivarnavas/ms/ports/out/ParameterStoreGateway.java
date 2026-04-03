package com.raulbolivarnavas.ms.ports.out;

import reactor.core.publisher.Mono;

public interface ParameterStoreGateway {
    Mono<String> getParameter(String name);
}
