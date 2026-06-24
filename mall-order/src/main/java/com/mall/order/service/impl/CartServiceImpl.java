package com.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.order.dto.CartDTO;
import com.mall.order.dto.ProductSimpleDTO;
import com.mall.order.entity.Cart;
import com.mall.order.feign.ProductFeignClient;
import com.mall.order.mapper.CartMapper;
import com.mall.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 *
 * <p>实现购物车的增删改查功能，通过 Feign 调用商品服务获取商品信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductFeignClient productFeignClient;

    /**
     * 获取用户的购物车列表
     *
     * <p>查询用户的购物车项，并通过 Feign 调用商品服务获取商品信息。</p>
     *
     * @param userId 用户 ID
     * @return 购物车列表（包含商品信息）
     */
    @Override
    public List<CartDTO> listCart(Long userId) {
        List<Cart> cartList = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .orderByDesc(Cart::getCreateTime));

        return cartList.stream().map(cart -> {
            CartDTO dto = new CartDTO();
            dto.setId(cart.getId());
            dto.setProductId(cart.getProductId());
            dto.setQuantity(cart.getQuantity());
            dto.setChecked(cart.getChecked());

            // 通过 Feign 获取商品信息
            try {
                R<ProductSimpleDTO> productResult = productFeignClient.getProduct(cart.getProductId());
                if (productResult != null && productResult.getCode() == 200 && productResult.getData() != null) {
                    ProductSimpleDTO product = productResult.getData();
                    dto.setProductName(product.getName());
                    dto.setProductImage(product.getImage());
                    dto.setPrice(product.getPrice());
                    dto.setStock(product.getStock());
                }
            } catch (Exception e) {
                dto.setProductName("商品信息获取失败");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 添加商品到购物车
     *
     * <p>如果商品已存在，则增加数量；否则新增购物车项。</p>
     *
     * @param userId    用户 ID
     * @param productId 商品 ID
     * @param quantity  数量
     */
    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Cart existing = cartMapper.selectOne(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getProductId, productId));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setChecked(1);
            cartMapper.insert(cart);
        }
    }

    /**
     * 修改购物车项数量
     *
     * <p>数量小于等于 0 时删除该项。</p>
     *
     * @param userId   用户 ID
     * @param cartId   购物车项 ID
     * @param quantity 新数量
     */
    @Override
    public void updateQuantity(Long userId, Long cartId, int quantity) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        if (quantity <= 0) {
            cartMapper.deleteById(cartId);
        } else {
            cart.setQuantity(quantity);
            cartMapper.updateById(cart);
        }
    }

    /**
     * 删除购物车项
     *
     * @param userId 用户 ID
     * @param cartId 购物车项 ID
     */
    @Override
    public void deleteCartItem(Long userId, Long cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartMapper.deleteById(cartId);
    }

    /**
     * 更新购物车项选中状态
     *
     * @param userId  用户 ID
     * @param cartId  购物车项 ID
     * @param checked 选中状态：0=未选中，1=选中
     */
    @Override
    public void updateChecked(Long userId, Long cartId, int checked) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cart.setChecked(checked);
        cartMapper.updateById(cart);
    }
}
