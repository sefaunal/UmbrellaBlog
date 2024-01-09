package com.sefaunal.apigateway.Config;

import com.sefaunal.apigateway.Filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author github.com/sefaunal
 * @since 2024-01-11
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public GlobalFilter authenticationFilter() {
        return authenticationFilter;
    }
}
