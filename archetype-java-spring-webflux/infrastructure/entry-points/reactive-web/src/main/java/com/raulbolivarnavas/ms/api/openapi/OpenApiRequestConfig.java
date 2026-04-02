package com.raulbolivarnavas.ms.api.openapi;

public final class OpenApiRequestConfig {

    public static final String CREATE_API_MOCK_REQUEST = """
    {
      "method": "GET",
      "path": "/customers/{id}",
      "statusCode": 200,
      "responseBody": "{\"id\":\"123\",\"name\":\"Test Customer\"}",
      "headers": {
        "Content-Type": "application/json"
      },
      "delay": 0,
      "active": true,
      "description": "Mock para obtener cliente por ID"
    }
    """;

    public static final String LOAD_API_MOCKS_FROM_SQL_REQUEST = """
    {
      "sql": "SELECT * FROM api_mocks WHERE active = true",
      "dataSource": "primary"
    }
    """;

    public static final String UPDATE_API_MOCK_REQUEST = """
    {
      "method": "POST",
      "path": "/customers",
      "statusCode": 201,
      "responseBody": "{\"id\":\"456\",\"name\":\"New Customer\"}",
      "headers": {
        "Content-Type": "application/json"
      },
      "delay": 150,
      "active": true,
      "description": "Mock para crear cliente"
    }
    """;

    private OpenApiRequestConfig() {
    }
}
