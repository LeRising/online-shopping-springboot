package com.mall.common.config;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限拦截器
 *
 * <p>处理 {@link RequireAdmin} 注解，校验当前用户是否具有管理员权限。</p>
 *
 * <p>工作流程：</p>
 * <ol>
 *   <li>检查请求是否映射到 Controller 方法</li>
 *   <li>检查方法是否标注了 @RequireAdmin 注解</li>
 *   <li>从 UserContext 获取用户角色并校验</li>
 *   <li>角色不是管理员（role != 1）时返回 403</li>
 * </ol>
 *
 * @author risinglee
 * @since 1.0.0
 * @see RequireAdmin
 * @see UserContext
 */
public class AdminInterceptor implements HandlerInterceptor {

    /**
     * 请求预处理：校验管理员权限
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return true-放行，false-拦截
     */
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
