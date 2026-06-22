package com.mall.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.user.dto.LoginDTO;
import com.mall.user.dto.RegisterDTO;
import com.mall.user.dto.UserDTO;
import com.mall.user.entity.User;
import com.mall.user.mapper.UserMapper;
import com.mall.user.service.UserService;
import com.mall.common.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String USER_CACHE_KEY = "user:info:";
    private static final long CACHE_TTL_MINUTES = 30;

    @Override
    public void register(RegisterDTO dto) {
        // 检查用户名是否已存在
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
        user.setRole(0); // 默认普通用户

        userMapper.insert(user);
    }

    @Override
    public UserDTO login(LoginDTO dto, HttpSession session) {
        // 根据用户名查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 将用户信息写入Session
        UserContext.setUser(session, user.getId(), user.getUsername(), user.getRole());

        // 返回用户DTO（不含密码）
        return toUserDTO(user);
    }

    @Override
    public UserDTO getUserInfo(Long userId) {
        // 先查Redis缓存
        String cacheKey = USER_CACHE_KEY + userId;
        String cached = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cn.hutool.json.JSONUtil.toBean(cached, UserDTO.class);
        }

        // 缓存未命中，查数据库
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserDTO userDTO = toUserDTO(user);

        // 写入Redis缓存，TTL 30分钟
        stringRedisTemplate.opsForValue().set(cacheKey,
                cn.hutool.json.JSONUtil.toJsonStr(userDTO),
                CACHE_TTL_MINUTES, TimeUnit.MINUTES);

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

        // 清除缓存
        stringRedisTemplate.delete(USER_CACHE_KEY + userId);
    }

    @Override
    public long count() {
        return userMapper.selectCount(null);
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
