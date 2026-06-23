package com.mall.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 白名单路径 */
    private static final List<String> WHITELIST = Arrays.asList(
            "/api/user/register",
            "/api/user/login",
            "/api/product/list",
            "/api/product/hot",
            "/api/product/new",
            "/api/product/category/list",
            "/api/banner/list",
            "/images/**",
            "/feign/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单直接放行
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        // 获取 Token
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        final String token = authHeader.substring(7);

        // 调用 mall-user 验证 Token
        return webClient.get()
                .uri("http://localhost:8081/api/user/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        JsonNode root = objectMapper.readTree(body);
                        JsonNode data = root.path("data");

                        if (data.isMissingNode() || data.isNull()) {
                            return unauthorized(exchange);
                        }

                        Long userId = data.path("id").asLong(0);
                        if (userId == 0) {
                            return unauthorized(exchange);
                        }

                        String username = data.path("username").asText("");
                        int role = data.path("role").asInt(0);

                        ServerHttpRequest mutatedRequest = request.mutate()
                                .header("X-User-Id", String.valueOf(userId))
                                .header("X-Username", username)
                                .header("X-User-Role", String.valueOf(role))
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    } catch (Exception e) {
                        return unauthorized(exchange);
                    }
                })
                .onErrorResume(e -> unauthorized(exchange));
    }

    private boolean isWhitelisted(String path) {
        return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"code\":401,\"msg\":\"请先登录\",\"data\":null}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
