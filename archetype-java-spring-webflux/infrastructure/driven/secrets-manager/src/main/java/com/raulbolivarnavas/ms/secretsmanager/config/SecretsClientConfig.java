package com.raulbolivarnavas.ms.secretsmanager.config;

import io.github.raulbolivar.aws.secrets.adapter.SecretsManagerAdapter;
import io.github.raulbolivar.aws.secrets.config.SecretsManagerProperties;
import io.github.raulbolivar.aws.secrets.port.SecretsManagerPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SecretsClientConfig {

    @Bean
    @Profile("!local")
    public SecretsManagerPort secretsManagerPort(
            @Value("${aws.region:us-east-1}") String region,
            @Value("${aws.secrets.cache-ttl:3600}") long cacheTtl
    ) {
        return SecretsManagerAdapter.create(
                SecretsManagerProperties.builder()
                        .region(region)
                        .cacheTtlSeconds(cacheTtl)
                        .build()
        );
    }

    @Bean
    @Profile("local")
    public SecretsManagerPort secretsManagerLocalPort(
            @Value("${aws.region:us-east-1}") String region,
            @Value("${aws.secrets.cache-ttl:3600}") long cacheTtl
    ) {
        return SecretsManagerAdapter.create(
                SecretsManagerProperties.builder()
                        .region(region)
                        .endpointOverride("http://localhost:4566") // LocalStack default
                        .accessKeyId("test")
                        .secretAccessKey("test")
                        .cacheTtlSeconds(cacheTtl)
                        .build()
        );
    }

}
