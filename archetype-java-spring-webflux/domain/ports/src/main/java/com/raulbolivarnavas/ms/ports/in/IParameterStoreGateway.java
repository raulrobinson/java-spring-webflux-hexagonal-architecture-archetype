package com.raulbolivarnavas.ms.ports.in;

import reactor.core.publisher.Mono;

public interface IParameterStoreGateway {
    Mono<String> getParameter(String name);
}
