package com.raulbolivarnavas.ms.usecase;

import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import com.raulbolivarnavas.ms.model.ApiMockRuntimeInfo;
import com.raulbolivarnavas.ms.ports.in.IApiMocksUseCase;
import com.raulbolivarnavas.ms.ports.out.ApiMockPersistenceGateway;
import com.raulbolivarnavas.ms.ports.out.ApiMockRuntimeGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiMocksUseCase implements IApiMocksUseCase {

    private final ApiMockPersistenceGateway persistenceGateway;
    private final ApiMockRuntimeGateway runtimeGateway;

    @Value("${api-mocks.scripts-dir:./third-parties/api-mocks/sql}")
    private String scriptsDir;

    @Value("${api-mocks.auto-load-scripts:true}")
    private boolean autoLoadScripts;

    @Override
    public Mono<List<ApiMockDefinition>> list() {
        return persistenceGateway.list()
                .doOnSuccess(list -> log.info("[API-MOCKS] Loaded {} mock definitions", list.size()))
                .doOnError(error -> log.error("[API-MOCKS] Error listing mocks: {}", error.getMessage()));
    }

    @Override
    public Mono<ApiMockDefinition> get(Long id) {
        return persistenceGateway.findById(id)
                .doOnSuccess(mock -> log.info("[API-MOCKS] Loaded mock {}", id))
                .doOnError(error -> log.error("[API-MOCKS] Error loading mock {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<ApiMockDefinition> create(ApiMockDefinition definition) {
        ApiMockDefinition normalized = normalizeForSave(definition);
        validate(normalized);

        return persistenceGateway.create(normalized)
                .flatMap(saved -> syncRuntime().thenReturn(saved))
                .doOnSuccess(saved -> log.info("[API-MOCKS] Created mock {}", saved.id()))
                .doOnError(error -> log.error("[API-MOCKS] Error creating mock: {}", error.getMessage()));
    }

    @Override
    public Mono<ApiMockDefinition> update(Long id, ApiMockDefinition definition) {
        ApiMockDefinition normalized = normalizeForSave(definition);
        validate(normalized);

        return persistenceGateway.update(id, normalized)
                .flatMap(updated -> syncRuntime().thenReturn(updated))
                .doOnSuccess(updated -> log.info("[API-MOCKS] Updated mock {}", id))
                .doOnError(error -> log.error("[API-MOCKS] Error updating mock {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return persistenceGateway.delete(id)
                .then(syncRuntime())
                .doOnSuccess(ignored -> log.info("[API-MOCKS] Deleted mock {}", id))
                .doOnError(error -> log.error("[API-MOCKS] Error deleting mock {}: {}", id, error.getMessage()));
    }

    @Override
    public Mono<Integer> reloadScriptedMocks() {
        return persistenceGateway.reloadScriptedMocks()
                .flatMap(count -> syncRuntime().thenReturn(count))
                .doOnSuccess(count -> log.info("[API-MOCKS] Reloaded {} scripted mock records", count))
                .doOnError(error -> log.error("[API-MOCKS] Error reloading scripted mocks: {}", error.getMessage()));
    }

    @Override
    public Mono<Integer> loadSqlScript(String sqlScript, boolean replaceScripted) {
        String normalized = trimToEmpty(sqlScript);
        if (normalized.isBlank()) {
            return Mono.error(new IllegalArgumentException("SQL script content is required"));
        }

        return persistenceGateway.loadMocksFromSql(normalized, replaceScripted)
                .flatMap(count -> syncRuntime().thenReturn(count))
                .doOnSuccess(count -> log.info("[API-MOCKS] Loaded SQL script into H2. Total records: {}", count))
                .doOnError(error -> log.error("[API-MOCKS] Error loading SQL script: {}", error.getMessage()));
    }

    @Override
    public Mono<ApiMockRuntimeInfo> runtimeInfo() {
        return Mono.just(new ApiMockRuntimeInfo(
                runtimeGateway.baseUrl(),
                runtimeGateway.port(),
                scriptsDir,
                autoLoadScripts
        ));
    }

    @Override
    public Mono<Void> syncRuntime() {
        return persistenceGateway.list()
                .flatMap(runtimeGateway::syncMocks)
                .doOnSuccess(ignored -> log.info("[API-MOCKS] WireMock runtime synchronized"))
                .doOnError(error -> log.error("[API-MOCKS] Error synchronizing runtime: {}", error.getMessage()));
    }

    private ApiMockDefinition normalizeForSave(ApiMockDefinition definition) {
        String source = definition.source();
        if (source == null || source.isBlank()) {
            source = "ui";
        }

        Map<String, String> requestHeaders = definition.requestHeaders() == null
                ? Map.of()
                : new LinkedHashMap<>(definition.requestHeaders());
        Map<String, String> responseHeaders = definition.responseHeaders() == null
                ? Map.of()
                : new LinkedHashMap<>(definition.responseHeaders());

        return new ApiMockDefinition(
                definition.id(),
                trimToEmpty(definition.name()),
                trimToNull(definition.description()),
                trimToEmpty(definition.method()).toUpperCase(),
                ensureLeadingSlash(trimToEmpty(definition.path())),
                definition.priority() != null ? definition.priority() : 100,
                definition.enabled(),
                source,
                requestHeaders,
                trimToNull(definition.requestBody()),
                definition.responseStatus() != null ? definition.responseStatus() : 200,
                responseHeaders,
                definition.responseBody() != null ? definition.responseBody() : "",
                definition.createdAt(),
                definition.updatedAt()
        );
    }

    private void validate(ApiMockDefinition definition) {
        if (definition.name().isBlank()) {
            throw new IllegalArgumentException("Mock name is required");
        }
        if (definition.method().isBlank()) {
            throw new IllegalArgumentException("HTTP method is required");
        }
        if (definition.path().isBlank()) {
            throw new IllegalArgumentException("Path is required");
        }
        if (definition.responseStatus() == null || definition.responseStatus() < 100 || definition.responseStatus() > 599) {
            throw new IllegalArgumentException("Response status must be between 100 and 599");
        }
    }

    private String ensureLeadingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.startsWith("/") ? value : "/" + value;
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

