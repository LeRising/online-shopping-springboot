package com.mall.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.exception.BusinessException;
import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.order.dto.*;
import com.mall.order.entity.Cart;
import com.mall.order.entity.OrderInfo;
import com.mall.order.entity.OrderItem;
import com.mall.order.feign.ProductFeignClient;
import com.mall.order.feign.UserFeignClient;
import com.mall.order.mapper.CartMapper;
import com.mall.order.mapper.OrderInfoMapper;
import com.mall.order.mapper.OrderItemMapper;
import com.mall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderInfoMapper orderInfoMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductFeignClient productFeignClient;
    private final UserFeignClient userFeignClient;

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, CreateOrderDTO dto) {
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId);
        if (dto.getCartIds() != null && !dto.getCartIds().isEmpty()) {
            cartWrapper.in(Cart::getId, dto.getCartIds());
        }
        List<Cart> cartList = cartMapper.selectList(cartWrapper);
        if (cartList.isEmpty()) {
            throw new BusinessException("购物车为空，请先添加商品");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Cart cart : cartList) {
            R<ProductSimpleDTO> productResult;
            try {
                productResult = productFeignClient.getProduct(cart.getProductId());
            } catch (Exception e) {
                log.error("Feign调用商品服务失败, productId={}: {}", cart.getProductId(), e.getMessage());
                throw new BusinessException("商品服务调用失败，请确认商品服务已启动");
            }
            if (productResult == null || productResult.getCode() != 200 || productResult.getData() == null) {
                throw new BusinessException("商品信息获取失败: " + cart.getProductId());
            }
            ProductSimpleDTO product = productResult.getData();

            OrderItem item = new OrderItem();
            item.setProductId(cart.getProductId());
            item.setProductName(product.getName());
            item.setProductImage(product.getImage());
            item.setPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            orderItems.add(item);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        }

        for (OrderItem item : orderItems) {
            try {
                R<Void> deductResult = productFeignClient.deductStock(item.getProductId(), item.getQuantity());
                if (deductResult == null || deductResult.getCode() != 200) {
                    throw new BusinessException("库存扣减失败: " + item.getProductName());
                }
            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                log.error("扣减库存异常, productId={}: {}", item.getProductId(), e.getMessage());
                throw new BusinessException("库存扣减失败: " + e.getMessage());
            }
        }

        // 获取收货地址快照
        String addressSnapshot = "{}";
        try {
            Map<String, Object> addrData = null;

            if (dto.getAddressId() != null) {
                R<Map<String, Object>> addrResult = userFeignClient.getAddress(userId, dto.getAddressId());
                if (addrResult != null && addrResult.getCode() == 200 && addrResult.getData() != null) {
                    addrData = addrResult.getData();
                }
            } else {
                R<List<Map<String, Object>>> addrListResult = userFeignClient.getAddresses(userId);
                if (addrListResult != null && addrListResult.getCode() == 200
                        && addrListResult.getData() != null && !addrListResult.getData().isEmpty()) {
                    List<Map<String, Object>> addresses = addrListResult.getData();
                    addrData = addresses.stream()
                            .filter(a -> a.get("isDefault") != null && "1".equals(String.valueOf(a.get("isDefault"))))
                            .findFirst()
                            .orElse(addresses.get(0));
                }
            }

            if (addrData != null) {
                addressSnapshot = addrData.toString();
            }
        } catch (Exception e) {
            log.warn("获取收货地址失败: {}", e.getMessage());
        }

        OrderInfo order = new OrderInfo();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setAddressSnapshot(addressSnapshot);
        orderInfoMapper.insert(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        for (Cart cart : cartList) {
            cartMapper.deleteById(cart.getId());
        }

        return getOrderDetail(userId, order.getId());
    }

    @Override
    public PageResult<OrderDTO> listOrders(Long userId, Integer status, int page, int size) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId);
        if (status != null) wrapper.eq(OrderInfo::getStatus, status);
        wrapper.orderByDesc(OrderInfo::getCreateTime);

        Page<OrderInfo> pageResult = orderInfoMapper.selectPage(new Page<>(page, size), wrapper);
        List<OrderDTO> records = pageResult.getRecords().stream().map(this::toOrderDTO).collect(Collectors.toList());
        return new PageResult<>(records, pageResult.getTotal(), page, size);
    }

    @Override
    public OrderDTO getOrderDetail(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        return toOrderDTO(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("只能取消待付款订单");
        }
        order.setStatus(4);
        orderInfoMapper.updateById(order);

        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : items) {
            try {
                productFeignClient.restoreStock(item.getProductId(), item.getQuantity());
            } catch (Exception e) {
                log.error("恢复库存失败, productId={}: {}", item.getProductId(), e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void payOrder(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("订单状态不正确");
        }
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional
    public void confirmOrder(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("只能确认已发货的订单");
        }
        order.setStatus(3);
        orderInfoMapper.updateById(order);
    }

    @Override
    @Transactional
    public void applyReturn(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new BusinessException("只能对已付款或已发货的订单申请退货");
        }
        order.setStatus(5);
        orderInfoMapper.updateById(order);

        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : items) {
            try {
                productFeignClient.restoreStock(item.getProductId(), item.getQuantity());
            } catch (Exception e) {
                log.error("退货恢复库存失败, productId={}: {}", item.getProductId(), e.getMessage());
            }
        }
    }

    @Override
    public PageResult<OrderDTO> listAllOrders(Integer status, int page, int size) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(OrderInfo::getStatus, status);
        wrapper.orderByDesc(OrderInfo::getCreateTime);

        Page<OrderInfo> pageResult = orderInfoMapper.selectPage(new Page<>(page, size), wrapper);
        List<OrderDTO> records = pageResult.getRecords().stream().map(this::toOrderDTO).collect(Collectors.toList());
        return new PageResult<>(records, pageResult.getTotal(), page, size);
    }

    @Override
    @Transactional
    public void shipOrder(Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getStatus() != 1) throw new BusinessException("只能对已付款订单发货");
        order.setStatus(2);
        orderInfoMapper.updateById(order);
    }

    private OrderDTO toOrderDTO(OrderInfo order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setStatusText();
        dto.setAddressSnapshot(order.getAddressSnapshot());
        dto.setPayTime(order.getPayTime());
        dto.setCreateTime(order.getCreateTime());

        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        List<OrderItemDTO> itemDTOs = items.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(item.getProductId());
            itemDTO.setProductName(item.getProductName());
            itemDTO.setProductImage(item.getProductImage());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setQuantity(item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList());
        dto.setItems(itemDTOs);
        return dto;
    }
}
