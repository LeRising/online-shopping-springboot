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

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
public class UserController {

    private final UserService userService;
    private final UserAddressService userAddressService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return R.ok();
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(userService.login(dto));
    }

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
     * 通过 Authorization Header 验证 Token 并返回用户信息
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

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public R<UserDTO> getUserInfo() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        return R.ok(userService.getUserInfo(userId));
    }

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

    @GetMapping("/address/list")
    @Operation(summary = "获取用户地址列表")
    public R<List<UserAddress>> addressList() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        return R.ok(userAddressService.listByUserId(userId));
    }

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
