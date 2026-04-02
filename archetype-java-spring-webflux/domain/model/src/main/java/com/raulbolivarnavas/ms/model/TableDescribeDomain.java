package com.raulbolivarnavas.ms.model;

import java.util.List;

public record TableDescribeDomain(
        String tableName,
        List<String> keySchema
) {}
