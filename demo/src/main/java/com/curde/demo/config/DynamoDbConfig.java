package com.curde.demo.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DynamoDbConfig {
    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedClient() {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(getDynamoDbClient())
                .build();
    }

    @Bean
    public DynamoDbAsyncClient getDynamoDbClient() {
            return DynamoDbAsyncClient.builder()
                    .endpointOverride(URI.create("http://localhost:8000"))
                    .region(Region.AP_SOUTHEAST_1)
                    .overrideConfiguration(ClientOverrideConfiguration.builder()
                            .apiCallTimeout(Duration.ofSeconds(10))
                            .apiCallAttemptTimeout(Duration.ofSeconds(5))
                            .build())
                    .build();

    }
}
