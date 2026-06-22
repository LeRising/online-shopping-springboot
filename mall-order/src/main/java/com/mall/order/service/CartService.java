package com.mall.order.service;

import com.mall.order.dto.CartDTO;
import java.util.List;

public interface CartService {
    List<CartDTO> listCart(Long userId);
    void addToCart(Long userId, Long productId, int quantity);
    void updateQuantity(Long userId, Long cartId, int quantity);
    void deleteCartItem(Long userId, Long cartId);
    void updateChecked(Long userId, Long cartId, int checked);
}
