package com.mall.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.user.dto.LoginDTO;
import com.mall.user.dto.RegisterDTO;
import com.mall.user.dto.UserDTO;
import com.mall.user.entity.User;
import com.mall.user.mapper.UserMapper;
import com.mall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** Token 存储：token -> userId */
    private static final Map<String, Long> TOKEN_STORE = new ConcurrentHashMap<>();
    /** 用户信息缓存：userId -> UserDTO */
    private static final Map<Long, UserDTO> USER_CACHE = new ConcurrentHashMap<>();

    @Override
    public void register(RegisterDTO dto) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(0);

        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 生成 Token
        String token = IdUtil.fastSimpleUUID();
        TOKEN_STORE.put(token, user.getId());

        // 缓存用户信息
        UserDTO userDTO = toUserDTO(user);
        USER_CACHE.put(user.getId(), userDTO);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("role", user.getRole());
        return result;
    }

    @Override
    public void logout(String token) {
        TOKEN_STORE.remove(token);
    }

    @Override
    public UserDTO getUserInfo(Long userId) {
        // 先查缓存
        UserDTO cached = USER_CACHE.get(userId);
        if (cached != null) {
            return cached;
        }

        // 缓存未命中，查数据库
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserDTO userDTO = toUserDTO(user);
        USER_CACHE.put(userId, userDTO);
        return userDTO;
    }

    @Override
    public void updateUserInfo(Long userId, UserDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());

        userMapper.updateById(user);
        USER_CACHE.remove(userId);
    }

    @Override
    public long count() {
        return userMapper.selectCount(null);
    }

    /** 验证 Token 并返回 userId */
    public Long validateToken(String token) {
        return TOKEN_STORE.get(token);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        return dto;
    }
}
