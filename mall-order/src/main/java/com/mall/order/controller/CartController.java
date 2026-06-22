package com.mall.order.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
import com.mall.order.dto.CartDTO;
import com.mall.order.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车接口")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "购物车列表")
    @GetMapping("/list")
    public R<List<CartDTO>> list() {
        Long userId = checkLogin();
        return R.ok(cartService.listCart(userId));
    }

    @Operation(summary = "添加购物车")
    @PostMapping("/add")
    public R<Void> add(@RequestParam Long productId,
                       @RequestParam(defaultValue = "1") int quantity) {
        Long userId = checkLogin();
        cartService.addToCart(userId, productId, quantity);
        return R.ok();
    }

    @Operation(summary = "修改数量")
    @PutMapping("/update")
    public R<Void> update(@RequestParam Long cartId, @RequestParam int quantity) {
        Long userId = checkLogin();
        cartService.updateQuantity(userId, cartId, quantity);
        return R.ok();
    }

    @Operation(summary = "删除购物车项")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        Long userId = checkLogin();
        cartService.deleteCartItem(userId, id);
        return R.ok();
    }

    @Operation(summary = "更新选中状态")
    @PutMapping("/check/{id}")
    public R<Void> updateCheck(@PathVariable Long id, @RequestParam int checked) {
        Long userId = checkLogin();
        cartService.updateChecked(userId, id, checked);
        return R.ok();
    }

    private Long checkLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}
