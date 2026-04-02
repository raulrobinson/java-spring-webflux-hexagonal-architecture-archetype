package com.raulbolivarnavas.ms.ports.out;

import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApiMockRuntimeGateway {

    Mono<Void> syncMocks(List<ApiMockDefinition> definitions);

    String baseUrl();

    int port();
}

