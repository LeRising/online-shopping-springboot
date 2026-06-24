package com.mall.user.controller;

import com.mall.common.result.R;
import com.mall.common.util.UserContext;
import com.mall.user.dto.AddressDTO;
import com.mall.user.dto.LoginDTO;
import com.mall.user.dto.RegisterDTO;
import com.mall.user.dto.UserDTO;
import com.mall.user.entity.UserAddress;
import com.mall.user.service.UserAddressService;
import com.mall.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * <p>提供用户的注册、登录、信息管理、收货地址管理等接口。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
public class UserController {

    private final UserService userService;
    private final UserAddressService userAddressService;

    /**
     * 用户注册
     *
     * @param dto 注册请求（包含用户名、密码等）
     * @return 操作结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return R.ok();
    }

    /**
     * 用户登录
     *
     * @param dto 登录请求（包含用户名、密码）
     * @return 登录结果（包含 token、用户信息）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(userService.login(dto));
    }

    /**
     * 用户登出
     *
     * @param token 用户 Token
     * @return 操作结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public R<Void> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            userService.logout(token.substring(7));
        }
        return R.ok();
    }

    /**
     * Token 验证接口（供 Gateway 调用，不需要认证）
     *
     * <p>通过 Authorization Header 验证 Token 并返回用户信息。</p>
     *
     * @param authHeader Authorization Header
     * @return 用户信息或错误信息
     */
    @GetMapping("/validate")
    @Operation(summary = "验证Token")
    public R<UserDTO> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return R.fail(401, "Token无效");
        }
        String token = authHeader.substring(7);
        Long userId = userService.validateToken(token);
        if (userId == null) {
            return R.fail(401, "Token已过期或无效");
        }
        return R.ok(userService.getUserInfo(userId));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public R<UserDTO> getUserInfo() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        return R.ok(userService.getUserInfo(userId));
    }

    /**
     * 更新用户信息
     *
     * @param dto 用户信息（只更新非空字段）
     * @return 操作结果
     */
    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public R<Void> updateUserInfo(@RequestBody UserDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userService.updateUserInfo(userId, dto);
        return R.ok();
    }

    /**
     * 获取用户收货地址列表
     *
     * @return 地址列表
     */
    @GetMapping("/address/list")
    @Operation(summary = "获取用户地址列表")
    public R<List<UserAddress>> addressList() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        return R.ok(userAddressService.listByUserId(userId));
    }

    /**
     * 添加收货地址
     *
     * @param dto 地址信息
     * @return 操作结果
     */
    @PostMapping("/address")
    @Operation(summary = "添加收货地址")
    public R<Void> addAddress(@Valid @RequestBody AddressDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userAddressService.addAddress(userId, dto);
        return R.ok();
    }

    /**
     * 更新收货地址
     *
     * @param dto 地址信息（包含地址 ID）
     * @return 操作结果
     */
    @PutMapping("/address")
    @Operation(summary = "更新收货地址")
    public R<Void> updateAddress(@Valid @RequestBody AddressDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userAddressService.updateAddress(userId, dto);
        return R.ok();
    }

    /**
     * 删除收货地址
     *
     * @param id 地址 ID
     * @return 操作结果
     */
    @DeleteMapping("/address/{id}")
    @Operation(summary = "删除收货地址")
    public R<Void> deleteAddress(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userAddressService.deleteAddress(userId, id);
        return R.ok();
    }
}
