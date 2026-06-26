package com.mall.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Web MVC 配置类
 *
 * <p>配置拦截器和跨域访问策略。</p>
 *
 * <p>拦截器配置：</p>
 * <ul>
 *   <li>登录拦截器 - 拦截需要登录的路径，排除公开接口</li>
 *   <li>管理员拦截器 - 拦截 /api/admin/** 路径，校验管理员权限</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** 需要登录的路径 */
    private static final List<String> AUTH_PATHS = Arrays.asList(
            "/api/user/info/**", "/api/user/address/**", "/api/user/logout",
            "/api/cart/**", "/api/order/**",
            "/api/admin/**"
    );

    /** 排除的路径（公开接口和文档） */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/user/register", "/api/user/login", "/api/user/validate",
            "/api/product/list", "/api/product/hot", "/api/product/new",
            "/api/product/category/list", "/api/banner/list",
            "/feign/**",
            "/doc.html", "/webjars/**", "/v3/api-docs/**"
    );

    /** 图片存储路径 */
    @Value("${file.upload.path:D:/CodeWorkPlace/IdeaCode/online-shopping-springboot/images/}")
    private String imageStoragePath;

    /**
     * 注册拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(AUTH_PATHS)
                .excludePathPatterns(EXCLUDE_PATHS);

        // 管理员权限拦截器（在登录拦截器之后执行）
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/api/admin/**");
    }

    /**
     * 配置静态资源映射
     *
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /images/** 路径映射到本地文件目录
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imageStoragePath);
    }

    /**
     * 配置跨域访问
     *
     * @param registry 跨域配置注册器
     */
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
