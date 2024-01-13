package com.sefaunal.umbrellagateway.Filter;

import com.sefaunal.umbrellagateway.Exception.InvalidAccessException;
import com.sefaunal.umbrellagateway.Exception.InvalidTokenFormatException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author github.com/sefaunal
 * @since 2024-12-23
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final com.sefaunal.umbrellagateway.Filter.RouteValidator routeValidator;
    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(com.sefaunal.umbrellagateway.Filter.RouteValidator routeValidator, WebClient.Builder webClientBuilder) {
        super(Config.class); // Ensure the Config class is passed correctly
        this.routeValidator = routeValidator;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String token = exchange.getRequest().getHeaders().getFirst("Authorization");

                if (token == null || token.isEmpty()) {
                    throw new InvalidAccessException("Missing auth headers");
                } else if (!token.startsWith("Bearer ")) {
                    throw new InvalidTokenFormatException("Invalid token format");
                }

                //TODO fix org.springframework.web.reactive.function.client.WebClientRequestException: Invalid scheme [lb]
                return webClientBuilder.baseUrl("lb://UmbrellaAuth")
                        .build()
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/auth/verify/token")  // Path to the validate endpoint
                                .queryParam("token", token)  // Query param with token
                                .build())
                        .retrieve()
                        .toEntity(Boolean.class)
                        .flatMap(responseEntity -> {
                            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null && responseEntity.getBody()) {
                                return chain.filter(exchange);
                            } else {
                                return Mono.error(new InvalidAccessException("Invalid token"));
                            }
                        });
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {}
}