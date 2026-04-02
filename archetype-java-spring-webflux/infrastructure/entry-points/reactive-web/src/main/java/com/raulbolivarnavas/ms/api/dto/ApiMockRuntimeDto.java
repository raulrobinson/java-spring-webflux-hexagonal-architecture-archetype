package com.raulbolivarnavas.ms.api.dto;

public record ApiMockRuntimeDto(
        String baseUrl,
        Integer port,
        String scriptsDirectory,
        boolean autoLoadScripts
) {
}

