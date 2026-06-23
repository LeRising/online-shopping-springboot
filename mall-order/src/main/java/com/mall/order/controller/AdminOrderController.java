package com.mall.order.controller;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.order.dto.OrderDTO;
import com.mall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员-订单接口")
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @RequireAdmin
    @Operation(summary = "所有订单列表")
    @GetMapping("/list")
    public R<PageResult<OrderDTO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(orderService.listAllOrders(status, page, size));
    }

    @RequireAdmin
    @Operation(summary = "发货")
    @PutMapping("/ship/{id}")
    public R<Void> ship(@PathVariable Long id) {
        orderService.shipOrder(id);
        return R.ok();
    }
}
