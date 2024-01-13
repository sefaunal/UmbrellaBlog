package com.sefaunal.umbrellagateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author github.com/sefaunal
 * @since 2024-12-23
 */
@Configuration
public class GatewayConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}