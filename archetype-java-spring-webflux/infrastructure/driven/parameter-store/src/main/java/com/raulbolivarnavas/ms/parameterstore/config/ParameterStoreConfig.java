package com.raulbolivarnavas.ms.parameterstore.config;

import io.github.raulbolivar.aws.parameter.adapter.ParameterStoreAdapter;
import io.github.raulbolivar.aws.parameter.config.ParameterStoreProperties;
import io.github.raulbolivar.aws.parameter.port.ParameterStorePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ParameterStoreConfig {

    @Bean
    @Profile("!local")
    public ParameterStorePort parameterStorePort(
            @Value("${aws.region:us-east-1}") String region,
            @Value("${aws.secrets.cache-ttl:3600}") long cacheTtl
    ) {
        return ParameterStoreAdapter.create(
                ParameterStoreProperties.builder()
                        .region(region)
                        .cacheTtlSeconds(cacheTtl)
                        .build()
        );
    }

    @Bean
    @Profile("local")
    public ParameterStorePort parameterStoreLocalPort(
            @Value("${aws.region:us-east-1}") String region,
            @Value("${aws.secrets.cache-ttl:3600}") long cacheTtl
    ) {
        return ParameterStoreAdapter.create(
                ParameterStoreProperties.builder()
                        .region(region)
                        .endpointOverride("http://localhost:4566") // LocalStack default
                        .accessKeyId("test")
                        .secretAccessKey("test")
                        .cacheTtlSeconds(cacheTtl)
                        .build()
        );
    }
}
