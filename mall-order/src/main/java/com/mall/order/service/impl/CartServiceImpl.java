package com.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.order.dto.CartDTO;
import com.mall.order.entity.Cart;
import com.mall.order.feign.ProductFeignClient;
import com.mall.order.mapper.CartMapper;
import com.mall.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductFeignClient productFeignClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CART_CACHE_PREFIX = "cart:list:";
    private static final long CART_CACHE_TTL = 10;

    @Override
    public List<CartDTO> listCart(Long userId) {
        String cacheKey = CART_CACHE_PREFIX + userId;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof List) {
                @SuppressWarnings("unchecked")
                List<CartDTO> list = (List<CartDTO>) cached;
                return list;
            }
        } catch (Exception e) { }

        List<Cart> cartList = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .orderByDesc(Cart::getCreateTime));

        List<CartDTO> result = cartList.stream().map(cart -> {
            CartDTO dto = new CartDTO();
            dto.setId(cart.getId());
            dto.setProductId(cart.getProductId());
            dto.setQuantity(cart.getQuantity());
            dto.setChecked(cart.getChecked());

            try {
                R<Map<String, Object>> productResult = productFeignClient.getProduct(cart.getProductId());
                if (productResult != null && productResult.getCode() == 200 && productResult.getData() != null) {
                    Map<String, Object> product = productResult.getData();
                    dto.setProductName((String) product.get("name"));
                    dto.setProductImage((String) product.get("image"));
                    Object price = product.get("price");
                    if (price instanceof Number) {
                        dto.setPrice(BigDecimal.valueOf(((Number) price).doubleValue()));
                    }
                    Object stock = product.get("stock");
                    if (stock instanceof Number) {
                        dto.setStock(((Number) stock).intValue());
                    }
                }
            } catch (Exception e) {
                dto.setProductName("商品信息获取失败");
            }
            return dto;
        }).collect(Collectors.toList());

        try {
            redisTemplate.opsForValue().set(cacheKey, result, CART_CACHE_TTL, TimeUnit.MINUTES);
        } catch (Exception ignored) { }

        return result;
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
        clearCartCache(userId);
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
        clearCartCache(userId);
    }

    @Override
    public void deleteCartItem(Long userId, Long cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartMapper.deleteById(cartId);
        clearCartCache(userId);
    }

    @Override
    public void updateChecked(Long userId, Long cartId, int checked) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cart.setChecked(checked);
        cartMapper.updateById(cart);
        clearCartCache(userId);
    }

    private void clearCartCache(Long userId) {
        try {
            redisTemplate.delete(CART_CACHE_PREFIX + userId);
        } catch (Exception ignored) { }
    }
}
