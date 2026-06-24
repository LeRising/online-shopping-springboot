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

/**
 * 网关全局认证过滤器
 *
 * <p>作为 Spring Cloud Gateway 的核心组件，负责统一处理所有请求的身份认证。</p>
 *
 * <p>认证流程：</p>
 * <ol>
 *   <li>检查请求路径是否在白名单中，是则直接放行</li>
 *   <li>从 Authorization Header 提取 Bearer Token</li>
 *   <li>调用 mall-user 服务的 /api/user/validate 接口验证 Token</li>
 *   <li>验证成功后，将用户信息（ID、用户名、角色）以 Header 形式传递给下游服务</li>
 *   <li>验证失败则返回 401 未授权响应</li>
 * </ol>
 *
 * <p>白名单设计说明：</p>
 * <ul>
 *   <li>注册/登录接口 - 用户未登录时需要访问</li>
 *   <li>商品/分类/轮播图列表 - 公开的浏览接口</li>
 *   <li>/images/** - 静态资源</li>
 *   <li>/feign/** - 服务内部调用，由服务间信任保证安全</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    /** 路径匹配器，支持 Ant 风格的通配符 */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /** WebClient 用于调用 mall-user 服务验证 Token */
    private final WebClient webClient = WebClient.create();

    /** JSON 解析器 */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 白名单路径列表（不需要认证的接口） */
    private static final List<String> WHITELIST = Arrays.asList(
            "/api/user/register",      // 用户注册
            "/api/user/login",         // 用户登录
            "/api/product/list",       // 商品列表
            "/api/product/hot",        // 热门商品
            "/api/product/new",        // 新品推荐
            "/api/product/category/list", // 分类列表
            "/api/product/*",          // 商品详情
            "/api/banner/list",        // 轮播图列表
            "/images/**",              // 静态资源
            "/feign/**"                // 服务内部调用
    );

    /**
     * 过滤器核心逻辑
     *
     * <p>处理所有经过网关的请求，进行身份认证和用户信息透传。</p>
     *
     * @param exchange 请求上下文
     * @param chain    过滤器链
     * @return Mono<Void> 响应流
     */
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

        // 调用 mall-user 服务验证 Token
        return webClient.get()
                .uri("http://localhost:8081/api/user/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        JsonNode root = objectMapper.readTree(body);
                        JsonNode data = root.path("data");

                        // 验证失败：data 为空
                        if (data.isMissingNode() || data.isNull()) {
                            return unauthorized(exchange);
                        }

                        // 验证失败：用户 ID 无效
                        Long userId = data.path("id").asLong(0);
                        if (userId == 0) {
                            return unauthorized(exchange);
                        }

                        // 提取用户信息
                        String username = data.path("username").asText("");
                        int role = data.path("role").asInt(0);

                        // 将用户信息添加到请求 Header，传递给下游服务
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

    /**
     * 检查路径是否在白名单中
     *
     * @param path 请求路径
     * @return true-在白名单中（放行），false-不在白名单中（需要认证）
     */
    private boolean isWhitelisted(String path) {
        return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 返回 401 未授权响应
     *
     * @param exchange 请求上下文
     * @return 包含 401 状态码和错误信息的响应
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"code\":401,\"msg\":\"请先登录\",\"data\":null}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 获取过滤器执行顺序
     *
     * <p>返回 -100，确保认证过滤器在其他过滤器之前执行。
     * 优先级越高（数值越小）越先执行。</p>
     *
     * @return 过滤器顺序值
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
