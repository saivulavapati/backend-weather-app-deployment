package com.weatherapp.api_gateway.filter;

import com.weatherapp.api_gateway.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.WebUtils;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Value("${spring.jwt.jwtCookieName}")
    private String jwtCookie;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        HttpCookie cookie = request.getCookies().getFirst(jwtCookie);

        if (cookie != null) {
            String token = cookie.getValue();

            try {
                String username = jwtUtils.getSubjectFromJwtToken(token);

                if (username != null && jwtUtils.validateJwtToken(token, username)) {
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header("Authenticated-Email", username)
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                } else {
                    return handleUnauthorized(exchange.getResponse());
                }
            } catch (Exception e) {
                return handleUnauthorized(exchange.getResponse());
            }
        } else {
            return handleUnauthorized(exchange.getResponse());
        }
    }



    private Mono<Void> handleUnauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}