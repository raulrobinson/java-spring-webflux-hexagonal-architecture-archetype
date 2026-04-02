package com.raulbolivarnavas.ms.api.dto;

import java.time.Instant;
import java.util.Map;

public record ApiMockDto(
        Long id,
        String name,
        String description,
        String method,
        String path,
        Integer priority,
        boolean enabled,
        String source,
        Map<String, String> requestHeaders,
        String requestBody,
        Integer responseStatus,
        Map<String, String> responseHeaders,
        String responseBody,
        Instant createdAt,
        Instant updatedAt
) {
}

