package com.mall.order.service;

import com.mall.order.dto.CartDTO;
import java.util.List;

/**
 * 购物车服务接口
 *
 * <p>提供购物车的增删改查功能。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface CartService {

    /**
     * 获取用户的购物车列表
     *
     * @param userId 用户 ID
     * @return 购物车列表（包含商品信息）
     */
    List<CartDTO> listCart(Long userId);

    /**
     * 添加商品到购物车
     *
     * @param userId    用户 ID
     * @param productId 商品 ID
     * @param quantity  数量
     */
    void addToCart(Long userId, Long productId, int quantity);

    /**
     * 修改购物车项数量
     *
     * @param userId   用户 ID
     * @param cartId   购物车项 ID
     * @param quantity 新数量（小于等于 0 时删除该项）
     */
    void updateQuantity(Long userId, Long cartId, int quantity);

    /**
     * 删除购物车项
     *
     * @param userId 用户 ID
     * @param cartId 购物车项 ID
     */
    void deleteCartItem(Long userId, Long cartId);

    /**
     * 更新购物车项选中状态
     *
     * @param userId  用户 ID
     * @param cartId  购物车项 ID
     * @param checked 选中状态：0=未选中，1=选中
     */
    void updateChecked(Long userId, Long cartId, int checked);
}
