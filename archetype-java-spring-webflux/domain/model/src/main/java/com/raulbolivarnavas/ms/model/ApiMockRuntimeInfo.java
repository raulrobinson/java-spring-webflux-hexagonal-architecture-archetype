package com.raulbolivarnavas.ms.model;

public record ApiMockRuntimeInfo(
        String baseUrl,
        Integer port,
        String scriptsDirectory,
        boolean autoLoadScripts
) {
}

