package com.sefaunal.umbrellagateway.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author github.com/sefaunal
 * @since 2024-12-23
 */
@Component
public class RouteValidator {
    //TODO move this to a yaml file
    public static final List<String> openAPIEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/verify",
            "/api/auth/demo",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openAPIEndpoints
                    .stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}