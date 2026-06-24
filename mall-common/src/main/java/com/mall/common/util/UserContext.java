package com.mall.common.util;

/**
 * 用户上下文（基于 ThreadLocal）
 *
 * <p>用于在同一个请求中传递用户信息。通过 Gateway 网关解析 Token 后，
 * 将用户信息以 Header 形式传递给下游服务，下游服务通过本类存储和获取用户信息。</p>
 *
 * <p>典型使用场景：</p>
 * <ul>
 *   <li>在拦截器中调用 {@link #setUser} 设置用户信息</li>
 *   <li>在 Controller/Service 中调用 {@link #getUserId} 获取当前用户 ID</li>
 *   <li>在请求结束时调用 {@link #clear} 清理资源</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
public class UserContext {

    /** 当前用户 ID */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /** 当前用户名 */
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    /** 当前用户角色：0-普通用户，1-管理员 */
    private static final ThreadLocal<Integer> ROLE = new ThreadLocal<>();

    /**
     * 设置当前请求的用户信息
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @param role     用户角色（0-普通用户，1-管理员）
     */
    public static void setUser(Long userId, String username, Integer role) {
        USER_ID.set(userId);
        USERNAME.set(username);
        ROLE.set(role);
    }

    /**
     * 获取当前用户 ID
     *
     * @return 用户 ID，未登录时返回 null
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名，未登录时返回 null
     */
    public static String getUsername() {
        return USERNAME.get();
    }

    /**
     * 获取当前用户角色
     *
     * @return 用户角色（0-普通用户，1-管理员），未登录时返回 null
     */
    public static Integer getRole() {
        return ROLE.get();
    }

    /**
     * 清理当前请求的用户上下文
     *
     * <p>应在请求结束时（拦截器的 afterCompletion 中）调用，防止内存泄漏</p>
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
        ROLE.remove();
    }
}
