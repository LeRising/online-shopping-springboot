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

/**
 * 购物车控制器
 *
 * <p>提供用户的购物车操作接口，包括：</p>
 * <ul>
 *   <li>查看购物车列表</li>
 *   <li>添加商品到购物车</li>
 *   <li>修改购物车项数量</li>
 *   <li>删除购物车项</li>
 *   <li>更新购物车项选中状态</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "购物车接口")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 获取购物车列表
     *
     * @return 购物车列表（包含商品信息）
     */
    @Operation(summary = "购物车列表")
    @GetMapping("/list")
    public R<List<CartDTO>> list() {
        Long userId = checkLogin();
        return R.ok(cartService.listCart(userId));
    }

    /**
     * 添加商品到购物车
     *
     * @param productId 商品 ID
     * @param quantity  数量（默认为 1）
     * @return 操作结果
     */
    @Operation(summary = "添加购物车")
    @PostMapping("/add")
    public R<Void> add(@RequestParam Long productId,
                       @RequestParam(defaultValue = "1") int quantity) {
        Long userId = checkLogin();
        cartService.addToCart(userId, productId, quantity);
        return R.ok();
    }

    /**
     * 修改购物车项数量
     *
     * @param cartId   购物车项 ID
     * @param quantity 新数量（小于等于 0 时删除该项）
     * @return 操作结果
     */
    @Operation(summary = "修改数量")
    @PutMapping("/update")
    public R<Void> update(@RequestParam Long cartId, @RequestParam int quantity) {
        Long userId = checkLogin();
        cartService.updateQuantity(userId, cartId, quantity);
        return R.ok();
    }

    /**
     * 删除购物车项
     *
     * @param id 购物车项 ID
     * @return 操作结果
     */
    @Operation(summary = "删除购物车项")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        Long userId = checkLogin();
        cartService.deleteCartItem(userId, id);
        return R.ok();
    }

    /**
     * 更新购物车项选中状态
     *
     * @param id      购物车项 ID
     * @param checked 选中状态：0=未选中，1=选中
     * @return 操作结果
     */
    @Operation(summary = "更新选中状态")
    @PutMapping("/check/{id}")
    public R<Void> updateCheck(@PathVariable Long id, @RequestParam int checked) {
        Long userId = checkLogin();
        cartService.updateChecked(userId, id, checked);
        return R.ok();
    }

    /**
     * 检查用户是否已登录
     *
     * @return 用户 ID
     * @throws BusinessException 未登录时抛出 401 异常
     */
    private Long checkLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}
