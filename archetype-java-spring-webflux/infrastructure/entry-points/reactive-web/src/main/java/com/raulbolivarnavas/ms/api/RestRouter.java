package com.raulbolivarnavas.ms.api;

import com.raulbolivarnavas.ms.api.handlers.ApiMocksHandler;
import com.raulbolivarnavas.ms.api.handlers.ParameterStoreHandler;
import com.raulbolivarnavas.ms.api.handlers.SecretsManagerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RestRouter {

    private final ApiMocksHandler apiMocksHandler;
    private final ParameterStoreHandler parameterStoreHandler;
    private final SecretsManagerHandler secretsManagerHandler;

    @Bean
    RouterFunction<ServerResponse> apiRoutes() {

        return RouterFunctions.route()
                .path("/api", b -> b

                        // ── Secrets Manager ─────────────────────────────
                        .GET("/secrets", secretsManagerHandler::getSecret)

                        // ── Parameter Store ─────────────────────────────
                        .GET("/parameters", parameterStoreHandler::getParameter)

                        // ── API Mocks ───────────────────────────────────
                        .GET("/api-mocks",           apiMocksHandler::list)
                        .GET("/api-mocks/{id}",      apiMocksHandler::get)
                        .POST("/api-mocks",          apiMocksHandler::create)
                        .PUT("/api-mocks/{id}",      apiMocksHandler::update)
                        .DELETE("/api-mocks/{id}",   apiMocksHandler::delete)
                        .POST("/api-mocks/reload",   apiMocksHandler::reload)
                        .POST("/api-mocks/load-sql", apiMocksHandler::loadSql)
                )
                .build();
    }


}
