package com.raulbolivarnavas.ms.persistence.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raulbolivarnavas.ms.model.ApiMockDefinition;
import com.raulbolivarnavas.ms.ports.out.ApiMockPersistenceGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiMockPersistenceAdapter implements ApiMockPersistenceGateway {

    private static final TypeReference<LinkedHashMap<String, String>> MAP_TYPE = new TypeReference<>() {};

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api-mocks.scripts-dir:./third-parties/api-mocks/sql}")
    private String scriptsDir;

    private final RowMapper<ApiMockDefinition> rowMapper = (rs, rowNum) -> new ApiMockDefinition(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("method"),
            rs.getString("path"),
            rs.getInt("priority"),
            rs.getBoolean("enabled"),
            rs.getString("source"),
            readMap(rs.getString("request_headers")),
            rs.getString("request_body"),
            rs.getInt("response_status"),
            readMap(rs.getString("response_headers")),
            rs.getString("response_body"),
            readInstant(rs.getObject("created_at", LocalDateTime.class)),
            readInstant(rs.getObject("updated_at", LocalDateTime.class))
    );

    @Override
    public Mono<List<ApiMockDefinition>> list() {
        return Mono.fromCallable(() -> jdbcTemplate.query("""
                        SELECT id, name, description, method, path, priority, enabled, source,
                               request_headers, request_body, response_status, response_headers,
                               response_body, created_at, updated_at
                        FROM api_mocks
                        ORDER BY priority ASC, id ASC
                        """, rowMapper))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ApiMockDefinition> findById(Long id) {
        return Mono.fromCallable(() -> {
                    List<ApiMockDefinition> result = jdbcTemplate.query("""
                                    SELECT id, name, description, method, path, priority, enabled, source,
                                           request_headers, request_body, response_status, response_headers,
                                           response_body, created_at, updated_at
                                    FROM api_mocks
                                    WHERE id = ?
                                    """,
                            rowMapper,
                            id);
                    if (result.isEmpty()) {
                        throw new NoSuchElementException("Api mock not found: " + id);
                    }
                    return result.getFirst();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ApiMockDefinition> create(ApiMockDefinition definition) {
        return Mono.fromCallable(() -> {
                    Instant now = Instant.now();
                    LocalDateTime nowLocal = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
                    jdbcTemplate.update("""
                                    INSERT INTO api_mocks (
                                        name, description, method, path, priority, enabled, source,
                                        request_headers, request_body, response_status, response_headers,
                                        response_body, created_at, updated_at
                                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                    """,
                            definition.name(),
                            definition.description(),
                            definition.method(),
                            definition.path(),
                            definition.priority(),
                            definition.enabled(),
                            definition.source(),
                            writeMap(definition.requestHeaders()),
                            definition.requestBody(),
                            definition.responseStatus(),
                            writeMap(definition.responseHeaders()),
                            definition.responseBody(),
                            nowLocal,
                            nowLocal
                    );

                    Long id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM api_mocks", Long.class);
                    if (id == null) {
                        throw new IllegalStateException("Failed to create api mock");
                    }
                    return findById(id).block();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ApiMockDefinition> update(Long id, ApiMockDefinition definition) {
        return Mono.fromCallable(() -> {
                    ApiMockDefinition current = findById(id).block();
                    if (current == null) {
                        throw new NoSuchElementException("Api mock not found: " + id);
                    }

                    int updated = jdbcTemplate.update("""
                                    UPDATE api_mocks
                                    SET name = ?,
                                        description = ?,
                                        method = ?,
                                        path = ?,
                                        priority = ?,
                                        enabled = ?,
                                        source = ?,
                                        request_headers = ?,
                                        request_body = ?,
                                        response_status = ?,
                                        response_headers = ?,
                                        response_body = ?,
                                        updated_at = ?
                                    WHERE id = ?
                                    """,
                            definition.name(),
                            definition.description(),
                            definition.method(),
                            definition.path(),
                            definition.priority(),
                            definition.enabled(),
                            definition.source() != null ? definition.source() : current.source(),
                            writeMap(definition.requestHeaders()),
                            definition.requestBody(),
                            definition.responseStatus(),
                            writeMap(definition.responseHeaders()),
                            definition.responseBody(),
                            LocalDateTime.now(ZoneOffset.UTC),
                            id
                    );

                    if (updated == 0) {
                        throw new NoSuchElementException("Api mock not found: " + id);
                    }
                    return findById(id).block();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> {
                    int updated = jdbcTemplate.update("DELETE FROM api_mocks WHERE id = ?", id);
                    if (updated == 0) {
                        throw new NoSuchElementException("Api mock not found: " + id);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Integer> reloadScriptedMocks() {
        return Mono.fromCallable(() -> {
                    Path dir = Path.of(scriptsDir);
                    if (!Files.exists(dir)) {
                        Files.createDirectories(dir);
                        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM api_mocks", Integer.class);
                    }

                    jdbcTemplate.update("DELETE FROM api_mocks WHERE source = 'script'");

                    List<Path> scripts = Files.list(dir)
                            .filter(path -> path.getFileName().toString().endsWith(".sql"))
                            .sorted()
                            .toList();

                    for (Path script : scripts) {
                        String sql = Files.readString(script, StandardCharsets.UTF_8).trim();
                        if (!sql.isBlank()) {
                            jdbcTemplate.execute(sql);
                            log.info("[API-MOCKS] Executed script {}", script.getFileName());
                        }
                    }

                    Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM api_mocks", Integer.class);
                    return count != null ? count : 0;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Integer> loadMocksFromSql(String sqlScript, boolean replaceScripted) {
        return Mono.fromCallable(() -> {
                    if (sqlScript == null || sqlScript.isBlank()) {
                        throw new IllegalArgumentException("SQL script content is required");
                    }

                    if (replaceScripted) {
                        jdbcTemplate.update("DELETE FROM api_mocks WHERE source = 'script'");
                    }

                    DataSource dataSource = jdbcTemplate.getDataSource();
                    if (dataSource == null) {
                        throw new IllegalStateException("DataSource is not available");
                    }

                    Connection connection = DataSourceUtils.getConnection(dataSource);
                    try {
                        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(sqlScript.getBytes(StandardCharsets.UTF_8)));
                    } finally {
                        DataSourceUtils.releaseConnection(connection, dataSource);
                    }

                    Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM api_mocks", Integer.class);
                    return count != null ? count : 0;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Map<String, String> readMap(String value) {
        if (value == null || value.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(value, MAP_TYPE);
        } catch (IOException error) {
            throw new IllegalStateException("Failed to parse persisted headers JSON", error);
        }
    }

    private String writeMap(Map<String, String> value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException error) {
            throw new IllegalStateException("Failed to serialize headers JSON", error);
        }
    }

    private Instant readInstant(LocalDateTime value) {
        return value != null ? value.toInstant(ZoneOffset.UTC) : null;
    }
}

