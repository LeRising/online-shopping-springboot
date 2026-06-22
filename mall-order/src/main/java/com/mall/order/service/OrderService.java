package com.mall.order.service;

import com.mall.common.result.PageResult;
import com.mall.order.dto.CreateOrderDTO;
import com.mall.order.dto.OrderDTO;

public interface OrderService {
    OrderDTO createOrder(Long userId, CreateOrderDTO dto);
    PageResult<OrderDTO> listOrders(Long userId, Integer status, int page, int size);
    OrderDTO getOrderDetail(Long userId, Long orderId);
    void cancelOrder(Long userId, Long orderId);
    void payOrder(Long userId, Long orderId);
    void confirmOrder(Long userId, Long orderId);
    void applyReturn(Long userId, Long orderId);
    PageResult<OrderDTO> listAllOrders(Integer status, int page, int size);
    void shipOrder(Long orderId);
}
