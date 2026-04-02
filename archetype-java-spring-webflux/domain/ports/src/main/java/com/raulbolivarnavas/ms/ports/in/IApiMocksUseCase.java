package com.raulbolivarnavas.ms.ports.in;

import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import com.raulbolivarnavas.ms.model.ApiMockRuntimeInfo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IApiMocksUseCase {
    Mono<List<ApiMockDefinition>> list();

    Mono<ApiMockDefinition> get(Long id);

    Mono<ApiMockDefinition> create(ApiMockDefinition definition);

    Mono<ApiMockDefinition> update(Long id, ApiMockDefinition definition);

    Mono<Void> delete(Long id);

    Mono<Integer> reloadScriptedMocks();

    Mono<Integer> loadSqlScript(String sqlScript, boolean replaceScripted);

    Mono<ApiMockRuntimeInfo> runtimeInfo();

    Mono<Void> syncRuntime();
}
