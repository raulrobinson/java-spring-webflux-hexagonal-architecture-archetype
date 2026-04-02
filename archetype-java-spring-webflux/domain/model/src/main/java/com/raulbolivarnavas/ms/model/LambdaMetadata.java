package com.raulbolivarnavas.ms.model;

public record LambdaMetadata(
        String functionName,
        String functionArn,
        String runtime,
        String handler,
        String lastModified,
        String description
) {}
