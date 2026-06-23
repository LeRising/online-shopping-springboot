package com.mall.common.config;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器
 * 处理 @RequireAdmin 注解，校验管理员权限
 */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理 Controller 方法
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 检查是否有 @RequireAdmin 注解
        RequireAdmin requireAdmin = handlerMethod.getMethodAnnotation(RequireAdmin.class);
        if (requireAdmin == null) {
            return true;
        }

        // 校验管理员权限
        Integer role = UserContext.getRole();
        if (role == null || role != 1) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无管理员权限\",\"data\":null}");
            return false;
        }

        return true;
    }
}
