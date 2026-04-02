package com.raulbolivarnavas.ms.api.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class OpenApiSchemasConfig {

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/api-mocks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "listApiMocks",
                summary = "Listar API Mocks",
                description = "Retorna la lista paginada de todos los API Mocks registrados. Soporta filtros opcionales por método HTTP y estado activo.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    ),
                    @Parameter(
                        name = "page",
                        in = ParameterIn.QUERY,
                        required = false,
                        description = "Número de página (base 0)",
                        example = "0"
                    ),
                    @Parameter(
                        name = "size",
                        in = ParameterIn.QUERY,
                        required = false,
                        description = "Elementos por página",
                        example = "20"
                    ),
                    @Parameter(
                        name = "method",
                        in = ParameterIn.QUERY,
                        required = false,
                        description = "Filtrar por método HTTP",
                        example = "GET"
                    ),
                    @Parameter(
                        name = "active",
                        in = ParameterIn.QUERY,
                        required = false,
                        description = "Filtrar por estado activo/inactivo"
                    )
                },
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Lista obtenida exitosamente",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "SUCCESS",
                                value = OpenApiResponseConfig.LIST_API_MOCKS_SUCCESS
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Petición malformada o con parámetros inválidos",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "BAD_REQUEST",
                                value = OpenApiResponseConfig.LIST_API_MOCKS_BAD_REQUEST
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.LIST_API_MOCKS_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/api-mocks",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "createApiMock",
                summary = "Crear API Mock",
                description = "Crea un nuevo API Mock con la definición de ruta, método HTTP, código de respuesta y cuerpo. Una vez creado queda disponible inmediatamente para interceptar peticiones.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    )
                },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                            name = "Request",
                            value = OpenApiRequestConfig.CREATE_API_MOCK_REQUEST
                        )
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Mock creado exitosamente",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "CREATED",
                                value = OpenApiResponseConfig.CREATE_API_MOCK_CREATED
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Petición malformada o con parámetros inválidos",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "BAD_REQUEST",
                                value = OpenApiResponseConfig.CREATE_API_MOCK_BAD_REQUEST
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflicto — el recurso ya existe",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "CONFLICT",
                                value = OpenApiResponseConfig.CREATE_API_MOCK_CONFLICT
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "422",
                        description = "Regla de negocio violada",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "UNPROCESSABLE_ENTITY",
                                value = OpenApiResponseConfig.CREATE_API_MOCK_UNPROCESSABLE_ENTITY
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.CREATE_API_MOCK_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/api-mocks/reload",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "reloadApiMocks",
                summary = "Recargar mocks en caliente",
                description = "Fuerza la recarga de todos los mocks desde la fuente de datos sin reiniciar el servicio. Útil tras modificaciones directas en base de datos o tras una carga SQL.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    )
                },
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Recarga ejecutada exitosamente",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "SUCCESS",
                                value = OpenApiResponseConfig.RELOAD_API_MOCKS_SUCCESS
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.RELOAD_API_MOCKS_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/api-mocks/load-sql",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "loadApiMocksFromSql",
                summary = "Cargar mocks desde SQL",
                description = "Ejecuta una sentencia SQL SELECT personalizada y carga los resultados como definiciones de mocks. Permite carga masiva desde otras fuentes. La sentencia debe retornar columnas compatibles con el esquema ApiMock.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    )
                },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                            name = "Request",
                            value = OpenApiRequestConfig.LOAD_API_MOCKS_FROM_SQL_REQUEST
                        )
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Mocks cargados exitosamente",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "SUCCESS",
                                value = OpenApiResponseConfig.LOAD_API_MOCKS_FROM_SQL_SUCCESS
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Petición malformada o con parámetros inválidos",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "BAD_REQUEST",
                                value = OpenApiResponseConfig.LOAD_API_MOCKS_FROM_SQL_BAD_REQUEST
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "422",
                        description = "Regla de negocio violada",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "UNPROCESSABLE_ENTITY",
                                value = OpenApiResponseConfig.LOAD_API_MOCKS_FROM_SQL_UNPROCESSABLE_ENTITY
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.LOAD_API_MOCKS_FROM_SQL_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/api-mocks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "getApiMock",
                summary = "Obtener API Mock por ID",
                description = "Retorna el detalle completo de un API Mock dado su identificador UUID.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    ),
                    @Parameter(
                        name = "id",
                        in = ParameterIn.PATH,
                        required = true,
                        description = "Identificador UUID del API Mock",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    )
                },
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Mock encontrado",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "SUCCESS",
                                value = OpenApiResponseConfig.GET_API_MOCK_SUCCESS
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Recurso no encontrado",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "NOT_FOUND",
                                value = OpenApiResponseConfig.GET_API_MOCK_NOT_FOUND
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.GET_API_MOCK_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/api-mocks/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "updateApiMock",
                summary = "Actualizar API Mock",
                description = "Reemplaza completamente la definición de un API Mock existente.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    ),
                    @Parameter(
                        name = "id",
                        in = ParameterIn.PATH,
                        required = true,
                        description = "Identificador UUID del API Mock",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    )
                },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                            name = "Request",
                            value = OpenApiRequestConfig.UPDATE_API_MOCK_REQUEST
                        )
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Mock actualizado exitosamente",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "SUCCESS",
                                value = OpenApiResponseConfig.UPDATE_API_MOCK_SUCCESS
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Petición malformada o con parámetros inválidos",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "BAD_REQUEST",
                                value = OpenApiResponseConfig.UPDATE_API_MOCK_BAD_REQUEST
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Recurso no encontrado",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "NOT_FOUND",
                                value = OpenApiResponseConfig.UPDATE_API_MOCK_NOT_FOUND
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "422",
                        description = "Regla de negocio violada",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "UNPROCESSABLE_ENTITY",
                                value = OpenApiResponseConfig.UPDATE_API_MOCK_UNPROCESSABLE_ENTITY
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.UPDATE_API_MOCK_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/api-mocks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            operation = @Operation(
                operationId = "deleteApiMock",
                summary = "Eliminar API Mock",
                description = "Elimina permanentemente un API Mock. La operación no es reversible.",
                tags = {"API Mocks"},
                parameters = {
                    @Parameter(
                        name = "Application-Id",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Identificador del sistema cliente",
                        example = "APP-BANCA-MOVIL"
                    ),
                    @Parameter(
                        name = "Source-Bank",
                        in = ParameterIn.HEADER,
                        required = true,
                        description = "Código del banco origen",
                        example = "0001"
                    ),
                    @Parameter(
                        name = "X-Correlation-Id",
                        in = ParameterIn.HEADER,
                        required = false,
                        description = "ID de trazabilidad distribuida. Se genera automáticamente si no se envía.",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    ),
                    @Parameter(
                        name = "id",
                        in = ParameterIn.PATH,
                        required = true,
                        description = "Identificador UUID del API Mock",
                        example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    )
                },
                responses = {
                    @ApiResponse(
                        responseCode = "204",
                        description = "Mock eliminado exitosamente"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Recurso no encontrado",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "NOT_FOUND",
                                value = OpenApiResponseConfig.DELETE_API_MOCK_NOT_FOUND
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                name = "INTERNAL_SERVER_ERROR",
                                value = OpenApiResponseConfig.DELETE_API_MOCK_INTERNAL_SERVER_ERROR
                            )
                        )
                    )
                }
            )
        )
    })
    public RouterFunction<ServerResponse> documentedRoutes() {
        return route()
            .GET("/__openapi-doc-only", req -> ServerResponse
                .notFound().build())
        .build();
    }
}
