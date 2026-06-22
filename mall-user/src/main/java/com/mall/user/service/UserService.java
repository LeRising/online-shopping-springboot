package com.mall.user.service;

import com.mall.user.dto.LoginDTO;
import com.mall.user.dto.RegisterDTO;
import com.mall.user.dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public interface UserService {

    /**
     * 用户注册
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录，将用户信息写入Session
     */
    UserDTO login(LoginDTO dto, HttpSession session);

    /**
     * 获取用户信息（带Redis缓存）
     */
    UserDTO getUserInfo(Long userId);

    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, UserDTO dto);

    /**
     * 统计用户数量
     */
    long count();
}
