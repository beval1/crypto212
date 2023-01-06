package com.crypto212.apigw.filters;

import com.crypto212.clients.auth.AuthClient;
import com.crypto212.clients.auth.JwtTokenClaims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    private final AuthClient authClient;

    public JwtAuthenticationFilter(@Lazy AuthClient authClient) {
        super(Config.class);
        this.authClient = authClient;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            final List<String> apiEndpoints = List.of(
                    "/auth/sign-in",
                    "/auth/sign-up"
            );

            Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                    .noneMatch(uri -> r.getURI().getPath().contains(uri));

            if (isApiSecured.test(request)) {
                if (!request.getHeaders().containsKey("Authorization")) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);

                    return response.setComplete();
                }

                final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

                //send request to validate token, if valid get response body containing roles and usedId
                JwtTokenClaims jwtTokenClaims = authClient.getTokenClaims(token);

                //append userId and roles to http headers
                exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", Objects.requireNonNull(jwtTokenClaims).getUserId())
                        .header("X-User-Roles", jwtTokenClaims.getRoles())
                        .build();
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
