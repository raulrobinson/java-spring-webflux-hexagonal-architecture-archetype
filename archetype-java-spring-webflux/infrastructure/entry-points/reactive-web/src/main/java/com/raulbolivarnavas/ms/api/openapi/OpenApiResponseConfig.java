package com.raulbolivarnavas.ms.api.openapi;

public final class OpenApiResponseConfig {

    public static final String LIST_API_MOCKS_SUCCESS = """
    {
      "content": [
        {
          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
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
      ],
      "totalElements": 1,
      "page": 0,
      "size": 20
    }
    """;

    public static final String LIST_API_MOCKS_BAD_REQUEST = """
    {
      "code": "BAD_REQUEST",
      "message": "Request body is invalid",
      "details": [
        "method: must not be blank",
        "path: must not be blank"
      ],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String LIST_API_MOCKS_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String CREATE_API_MOCK_CREATED = """
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "method": "GET",
      "path": "/customers/{id}",
      "statusCode": 200,
      "responseBody": "{\"id\":\"123\",\"name\":\"Test Customer\"}",
      "headers": {
        "Content-Type": "application/json",
        "X-Mock-Source": "api-mocks-service"
      },
      "delay": 0,
      "active": true,
      "description": "Mock para obtener cliente por ID",
      "createdAt": "2026-04-02T10:00:00Z",
      "updatedAt": "2026-04-02T10:30:00Z"
    }
    """;

    public static final String CREATE_API_MOCK_BAD_REQUEST = """
    {
      "code": "BAD_REQUEST",
      "message": "Request body is invalid",
      "details": [
        "method: must not be blank",
        "path: must not be blank"
      ],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String CREATE_API_MOCK_CONFLICT = """
    {
      "code": "MOCK_ALREADY_EXISTS",
      "message": "ApiMock for GET /customers/{id} already exists",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String CREATE_API_MOCK_UNPROCESSABLE_ENTITY = """
    {
      "code": "DOMAIN_RULE_VIOLATION",
      "message": "statusCode 999 is not a valid HTTP status code",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String CREATE_API_MOCK_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String RELOAD_API_MOCKS_SUCCESS = """
    {
      "loaded": 42,
      "message": "Mocks reloaded successfully",
      "timestamp": "2026-04-02T10:30:00Z"
    }
    """;

    public static final String RELOAD_API_MOCKS_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String LOAD_API_MOCKS_FROM_SQL_SUCCESS = """
    {
      "loaded": 42,
      "message": "Mocks reloaded successfully",
      "timestamp": "2026-04-02T10:30:00Z"
    }
    """;

    public static final String LOAD_API_MOCKS_FROM_SQL_BAD_REQUEST = """
    {
      "code": "BAD_REQUEST",
      "message": "Request body is invalid",
      "details": [
        "method: must not be blank",
        "path: must not be blank"
      ],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String LOAD_API_MOCKS_FROM_SQL_UNPROCESSABLE_ENTITY = """
    {
      "code": "DOMAIN_RULE_VIOLATION",
      "message": "statusCode 999 is not a valid HTTP status code",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String LOAD_API_MOCKS_FROM_SQL_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String GET_API_MOCK_SUCCESS = """
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
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

    public static final String GET_API_MOCK_NOT_FOUND = """
    {
      "code": "MOCK_NOT_FOUND",
      "message": "ApiMock with id 3fa85f64-5717-4562-b3fc-2c963f66afa6 not found",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks/3fa85f64-5717-4562-b3fc-2c963f66afa6"
    }
    """;

    public static final String GET_API_MOCK_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String UPDATE_API_MOCK_SUCCESS = """
    {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "method": "GET",
      "path": "/customers/{id}",
      "statusCode": 200,
      "responseBody": "{\"id\":\"123\",\"name\":\"Test Customer\"}",
      "headers": {
        "Content-Type": "application/json",
        "X-Mock-Source": "api-mocks-service"
      },
      "delay": 0,
      "active": true,
      "description": "Mock para obtener cliente por ID",
      "createdAt": "2026-04-02T10:00:00Z",
      "updatedAt": "2026-04-02T10:30:00Z"
    }
    """;

    public static final String UPDATE_API_MOCK_BAD_REQUEST = """
    {
      "code": "BAD_REQUEST",
      "message": "Request body is invalid",
      "details": [
        "method: must not be blank",
        "path: must not be blank"
      ],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String UPDATE_API_MOCK_NOT_FOUND = """
    {
      "code": "MOCK_NOT_FOUND",
      "message": "ApiMock with id 3fa85f64-5717-4562-b3fc-2c963f66afa6 not found",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks/3fa85f64-5717-4562-b3fc-2c963f66afa6"
    }
    """;

    public static final String UPDATE_API_MOCK_UNPROCESSABLE_ENTITY = """
    {
      "code": "DOMAIN_RULE_VIOLATION",
      "message": "statusCode 999 is not a valid HTTP status code",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String UPDATE_API_MOCK_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    public static final String DELETE_API_MOCK_NOT_FOUND = """
    {
      "code": "MOCK_NOT_FOUND",
      "message": "ApiMock with id 3fa85f64-5717-4562-b3fc-2c963f66afa6 not found",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks/3fa85f64-5717-4562-b3fc-2c963f66afa6"
    }
    """;

    public static final String DELETE_API_MOCK_INTERNAL_SERVER_ERROR = """
    {
      "code": "INTERNAL_ERROR",
      "message": "An unexpected error occurred",
      "details": [],
      "timestamp": "2026-04-02T10:30:00Z",
      "correlationId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "path": "/api/api-mocks"
    }
    """;

    private OpenApiResponseConfig() {
    }
}
