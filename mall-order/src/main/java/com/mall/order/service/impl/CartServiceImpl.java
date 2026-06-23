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

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductFeignClient productFeignClient;

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

    @Override
    public void deleteCartItem(Long userId, Long cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartMapper.deleteById(cartId);
    }

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
