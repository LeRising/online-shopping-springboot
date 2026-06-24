package com.mall.common.config;

import com.mall.common.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 *
 * <p>从 Gateway 网关透传的 Header 中提取用户信息，存储到 {@link UserContext} 中。</p>
 *
 * <p>工作流程：</p>
 * <ol>
 *   <li>Gateway 验证 Token 成功后，将用户信息以 Header 形式传递给下游服务</li>
 *   <li>本拦截器从 Header 中提取用户信息（X-User-Id、X-Username、X-User-Role）</li>
 *   <li>将用户信息存储到 UserContext（ThreadLocal），供后续业务代码使用</li>
 *   <li>请求结束后清理 UserContext，防止内存泄漏</li>
 * </ol>
 *
 * @author risinglee
 * @since 1.0.0
 * @see UserContext
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 请求预处理：从 Header 中提取用户信息
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return true-放行，false-拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 请求直接放行（浏览器跨域预检请求）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从 Header 获取用户信息（由 Gateway 设置）
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            try {
                UserContext.setUser(
                        Long.parseLong(userIdHeader),
                        request.getHeader("X-Username"),
                        request.getHeader("X-User-Role") != null ? Integer.parseInt(request.getHeader("X-User-Role")) : 0
                );
                return true;
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }
        }

        // 没有用户信息，返回 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\",\"data\":null}");
        return false;
    }

    /**
     * 请求完成后清理用户上下文
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @param ex       异常（如果有）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
