package com.sefaunal.umbrellagateway.Filter;

import com.sefaunal.umbrellagateway.Exception.InvalidAccessException;
import com.sefaunal.umbrellagateway.Exception.InvalidTokenFormatException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
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
    private final RouteValidator routeValidator;
    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(com.sefaunal.umbrellagateway.Filter.RouteValidator routeValidator, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String token = exchange.getRequest().getHeaders().getFirst("Authorization");

                if (StringUtils.isEmpty(token)) {
                    throw new InvalidAccessException("Missing auth headers");
                } else if (!token.startsWith("Bearer ")) {
                    throw new InvalidTokenFormatException("Invalid token format");
                }

                return webClientBuilder.baseUrl("lb://UmbrellaAuth")
                        .build()
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/auth/verify/token")  // Path to the validate endpoint
                                .build())
                        .header(HttpHeaders.AUTHORIZATION, token)
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