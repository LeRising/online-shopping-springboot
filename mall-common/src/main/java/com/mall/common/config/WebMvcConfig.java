package com.mall.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** 需要登录的路径 */
    private static final List<String> AUTH_PATHS = Arrays.asList(
            "/api/user/info/**", "/api/user/address/**", "/api/user/logout",
            "/api/cart/**", "/api/order/**",
            "/api/admin/**"
    );

    /** 排除的路径 */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/user/register", "/api/user/login", "/api/user/validate",
            "/api/product/list", "/api/product/hot", "/api/product/new",
            "/api/product/category/list", "/api/banner/list",
            "/feign/**",
            "/doc.html", "/webjars/**", "/v3/api-docs/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(AUTH_PATHS)
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
