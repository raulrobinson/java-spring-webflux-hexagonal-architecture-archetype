package com.raulbolivarnavas.ms.api.handlers;

import com.raulbolivarnavas.ms.api.dto.ApiMockDto;
import com.raulbolivarnavas.ms.api.mappers.ApiMocksDtoMapper;
import com.raulbolivarnavas.ms.ports.in.IApiMocksUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiMocksHandler {

    private final IApiMocksUseCase useCase;
    private final ApiMocksDtoMapper mapper;

    public Mono<ServerResponse> list(ServerRequest ignoredRequest) {
        return Mono.zip(useCase.list(), useCase.runtimeInfo())
                .flatMap(tuple -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of(
                                "items", mapper.toDtoList(tuple.getT1()),
                                "count", tuple.getT1().size(),
                                "runtime", mapper.toDto(tuple.getT2())
                        )))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        return useCase.get(id)
                .map(mapper::toDto)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ApiMockDto.class)
                .map(mapper::toDomain)
                .flatMap(useCase::create)
                .map(mapper::toDto)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(ApiMockDto.class)
                .map(mapper::toDomain)
                .flatMap(dto -> useCase.update(id, dto))
                .map(mapper::toDto)
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        return useCase.delete(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("status", "deleted", "id", id)))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> reload(ServerRequest ignoredRequest) {
        return useCase.reloadScriptedMocks()
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("status", "reloaded", "count", count)))
                .onErrorResume(this::handleError);
    }

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> loadSql(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .flatMap(body -> {
                    String sqlScript = body.get("scriptSql") instanceof String value ? value : "";
                    if (sqlScript.isBlank() && body.get("sql") instanceof String fallback) {
                        sqlScript = fallback;
                    }

                    boolean replaceScripted = true;
                    if (body.get("replaceScripted") instanceof Boolean raw) {
                        replaceScripted = raw;
                    }

                    return useCase.loadSqlScript(sqlScript, replaceScripted)
                            .flatMap(count -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of("status", "loaded", "count", count)));
                })
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        if (error instanceof IllegalArgumentException) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("error", error.getMessage()));
        }

        if (error instanceof NoSuchElementException) {
            return ServerResponse.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("error", error.getMessage()));
        }

        return ServerResponse.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("error", error.getMessage() != null ? error.getMessage() : "Unexpected error"));
    }
}
