package com.raulbolivarnavas.ms.apimocks.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import com.raulbolivarnavas.ms.ports.out.ApiMockRuntimeGateway;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
@Component
@RequiredArgsConstructor
public class WireMockRuntimeAdapter implements ApiMockRuntimeGateway {

    @Value("${api-mocks.wiremock-port:18080}")
    private int wireMockPort;

    private final ObjectMapper objectMapper;

    private WireMockServer wireMockServer;

    @PostConstruct
    void start() {
        wireMockServer = new WireMockServer(options().port(wireMockPort));
        wireMockServer.start();
        log.info("[API-MOCKS] WireMock server started on port {}", wireMockPort);
    }

    @PreDestroy
    void stop() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @Override
    public Mono<Void> syncMocks(List<ApiMockDefinition> definitions) {
        return Mono.fromRunnable(() -> {
                    ensureRunning();
                    wireMockServer.resetAll();

                    definitions.stream()
                            .filter(ApiMockDefinition::enabled)
                            .sorted(Comparator.comparingInt(def -> def.priority() != null ? def.priority() : 100))
                            .forEach(this::registerStub);

                    log.info("[API-MOCKS] Registered {} active WireMock stubs", definitions.stream().filter(ApiMockDefinition::enabled).count());
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public String baseUrl() {
        return "http://localhost:" + wireMockPort;
    }

    @Override
    public int port() {
        return wireMockPort;
    }

    private void ensureRunning() {
        if (wireMockServer == null || !wireMockServer.isRunning()) {
            throw new IllegalStateException("WireMock server is not running");
        }
    }

    private void registerStub(ApiMockDefinition definition) {
        MappingBuilder mapping = request(definition.method(), urlPathEqualTo(definition.path()));

        for (Map.Entry<String, String> entry : definition.requestHeaders().entrySet()) {
            mapping.withHeader(entry.getKey(), equalTo(entry.getValue()));
        }

        if (definition.requestBody() != null && !definition.requestBody().isBlank()) {
            mapping.withRequestBody(isJson(definition.requestBody())
                    ? equalToJson(definition.requestBody(), true, true)
                    : equalTo(definition.requestBody()));
        }

        if (definition.priority() != null) {
            mapping.atPriority(definition.priority());
        }

        ResponseDefinitionBuilder response = aResponse().withStatus(definition.responseStatus() != null ? definition.responseStatus() : 200);

        if (definition.responseBody() != null) {
            response.withBody(definition.responseBody());
        }

        for (Map.Entry<String, String> entry : definition.responseHeaders().entrySet()) {
            response.withHeader(entry.getKey(), entry.getValue());
        }

        wireMockServer.stubFor(mapping.willReturn(response));
    }

    private boolean isJson(String raw) {
        try {
            objectMapper.readTree(raw);
            return true;
        } catch (Exception error) {
            return false;
        }
    }
}

