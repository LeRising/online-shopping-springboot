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
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public R<UserDTO> login(@Valid @RequestBody LoginDTO dto, HttpSession session) {
        UserDTO userDTO = userService.login(dto, session);
        return R.ok(userDTO);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public R<Void> logout(HttpSession session) {
        session.invalidate();
        return R.ok();
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public R<UserDTO> getUserInfo(HttpSession session) {
        Long userId = UserContext.getUserId(session);
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        UserDTO userDTO = userService.getUserInfo(userId);
        return R.ok(userDTO);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public R<Void> updateUserInfo(HttpSession session, @RequestBody UserDTO dto) {
        Long userId = UserContext.getUserId(session);
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userService.updateUserInfo(userId, dto);
        return R.ok();
    }

    @GetMapping("/address/list")
    @Operation(summary = "获取用户地址列表")
    public R<List<UserAddress>> addressList(HttpSession session) {
        Long userId = UserContext.getUserId(session);
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        List<UserAddress> list = userAddressService.listByUserId(userId);
        return R.ok(list);
    }

    @PostMapping("/address")
    @Operation(summary = "添加收货地址")
    public R<Void> addAddress(HttpSession session, @Valid @RequestBody AddressDTO dto) {
        Long userId = UserContext.getUserId(session);
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userAddressService.addAddress(userId, dto);
        return R.ok();
    }

    @PutMapping("/address")
    @Operation(summary = "更新收货地址")
    public R<Void> updateAddress(HttpSession session, @Valid @RequestBody AddressDTO dto) {
        Long userId = UserContext.getUserId(session);
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userAddressService.updateAddress(userId, dto);
        return R.ok();
    }

    @DeleteMapping("/address/{id}")
    @Operation(summary = "删除收货地址")
    public R<Void> deleteAddress(HttpSession session, @PathVariable Long id) {
        Long userId = UserContext.getUserId(session);
        if (userId == null) {
            return R.fail(401, "未登录");
        }
        userAddressService.deleteAddress(userId, id);
        return R.ok();
    }
}
