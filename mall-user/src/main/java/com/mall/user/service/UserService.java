package com.mall.user.service;

import com.mall.user.dto.LoginDTO;
import com.mall.user.dto.RegisterDTO;
import com.mall.user.dto.UserDTO;

import java.util.Map;

public interface UserService {

    void register(RegisterDTO dto);

    Map<String, Object> login(LoginDTO dto);

    void logout(String token);

    UserDTO getUserInfo(Long userId);

    void updateUserInfo(Long userId, UserDTO dto);

    long count();

    /** 验证 Token 并返回 userId，无效返回 null */
    Long validateToken(String token);
}
