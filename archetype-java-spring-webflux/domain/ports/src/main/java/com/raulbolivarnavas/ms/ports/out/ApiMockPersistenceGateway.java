package com.raulbolivarnavas.ms.ports.out;

import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ApiMockPersistenceGateway {

    Mono<List<ApiMockDefinition>> list();

    Mono<ApiMockDefinition> findById(Long id);

    Mono<ApiMockDefinition> create(ApiMockDefinition definition);

    Mono<ApiMockDefinition> update(Long id, ApiMockDefinition definition);

    Mono<Void> delete(Long id);

    Mono<Integer> reloadScriptedMocks();

    Mono<Integer> loadMocksFromSql(String sqlScript, boolean replaceScripted);
}

