package com.raulbolivarnavas.ms.model;

import java.time.Instant;

public record SecretMetadataDomain(
        String name,
        String arn,
        String description,
        Instant lastChangedDate
) {}
