package com.sefaunal.apigateway.Filter;

import com.sefaunal.apigateway.Client.AuthServiceClient;
import com.sefaunal.apigateway.Util.EndpointUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author github.com/sefaunal
 * @since 2024-01-11
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {
    private final AuthServiceClient authServiceClient;

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestURI = exchange.getRequest().getPath().value();

        // Check if the request URI belongs to a secured endpoint
        if (EndpointUtils.isSecureEndpoint(requestURI)) {
            LOG.info("{} is secured. Redirecting to Auth Service for Validation", requestURI);

            // Extract the API token from the request headers or any other appropriate location
            String apiToken = exchange.getRequest().getHeaders().getFirst("Authorization");

            // Call the auth service to validate the API token
            boolean isTokenValid = callAuthServiceForTokenValidation(apiToken);

            if (!isTokenValid) {
                // Token is not valid, return a 403 Forbidden response
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);

                LOG.info("{} is secured. Token validation failed!", requestURI);
                return exchange.getResponse().setComplete();
            }
        }

        // Token is valid or the endpoint is public, continue with the request handling
        return chain.filter(exchange);
    }

    private boolean callAuthServiceForTokenValidation(String apiToken) {
        return apiToken != null && authServiceClient.isTokenValid(apiToken);
    }
}
