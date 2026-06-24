package com.mall.user.service;

import com.mall.user.dto.LoginDTO;
import com.mall.user.dto.RegisterDTO;
import com.mall.user.dto.UserDTO;

import java.util.Map;

/**
 * 用户服务接口
 *
 * <p>提供用户注册、登录、信息管理等功能。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param dto 注册请求（包含用户名、密码等）
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录
     *
     * @param dto 登录请求（包含用户名、密码）
     * @return 登录结果（包含 token、用户信息）
     */
    Map<String, Object> login(LoginDTO dto);

    /**
     * 用户登出
     *
     * @param token 用户 Token
     */
    void logout(String token);

    /**
     * 获取用户信息
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    UserDTO getUserInfo(Long userId);

    /**
     * 更新用户信息
     *
     * @param userId 用户 ID
     * @param dto    用户信息（只更新非空字段）
     */
    void updateUserInfo(Long userId, UserDTO dto);

    /**
     * 统计用户总数
     *
     * @return 用户数量
     */
    long count();

    /**
     * 验证 Token 并返回 userId
     *
     * @param token 用户 Token
     * @return 用户 ID，无效返回 null
     */
    Long validateToken(String token);
}
