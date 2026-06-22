package com.mall.common.util;

import jakarta.servlet.http.HttpSession;

/**
 * 用户上下文（基于 Session）
 * 用于在同一个请求中传递用户信息
 */
public class UserContext {

    private static final String SESSION_USER_ID = "userId";
    private static final String SESSION_USERNAME = "username";
    private static final String SESSION_ROLE = "role";

    public static void setUser(HttpSession session, Long userId, String username, Integer role) {
        session.setAttribute(SESSION_USER_ID, userId);
        session.setAttribute(SESSION_USERNAME, username);
        session.setAttribute(SESSION_ROLE, role);
    }

    public static Long getUserId(HttpSession session) {
        Object val = session.getAttribute(SESSION_USER_ID);
        return val != null ? Long.valueOf(val.toString()) : null;
    }

    public static String getUsername(HttpSession session) {
        Object val = session.getAttribute(SESSION_USERNAME);
        return val != null ? val.toString() : null;
    }

    public static Integer getRole(HttpSession session) {
        Object val = session.getAttribute(SESSION_ROLE);
        return val != null ? Integer.valueOf(val.toString()) : null;
    }

    public static void clear(HttpSession session) {
        session.removeAttribute(SESSION_USER_ID);
        session.removeAttribute(SESSION_USERNAME);
        session.removeAttribute(SESSION_ROLE);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_ID) != null;
    }
}
