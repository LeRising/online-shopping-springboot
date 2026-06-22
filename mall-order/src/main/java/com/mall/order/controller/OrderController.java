package com.mall.order.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
import com.mall.order.dto.CreateOrderDTO;
import com.mall.order.dto.OrderDTO;
import com.mall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单接口")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public R<OrderDTO> createOrder(@RequestBody CreateOrderDTO dto) {
        Long userId = checkLogin();
        return R.ok(orderService.createOrder(userId, dto));
    }

    @Operation(summary = "我的订单列表")
    @GetMapping("/list")
    public R<PageResult<OrderDTO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = checkLogin();
        return R.ok(orderService.listOrders(userId, status, page, size));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public R<OrderDTO> detail(@PathVariable Long id) {
        Long userId = checkLogin();
        return R.ok(orderService.getOrderDetail(userId, id));
    }

    @Operation(summary = "取消订单")
    @PutMapping("/cancel/{id}")
    public R<Void> cancel(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.cancelOrder(userId, id);
        return R.ok();
    }

    @Operation(summary = "模拟支付")
    @PostMapping("/pay/{id}")
    public R<Void> pay(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.payOrder(userId, id);
        return R.ok();
    }

    @Operation(summary = "确认收货")
    @PutMapping("/confirm/{id}")
    public R<Void> confirm(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.confirmOrder(userId, id);
        return R.ok();
    }

    @Operation(summary = "申请退货")
    @PutMapping("/return/{id}")
    public R<Void> applyReturn(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.applyReturn(userId, id);
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
