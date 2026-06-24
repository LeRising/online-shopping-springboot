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

/**
 * 管理员订单控制器
 *
 * <p>提供管理员的订单管理接口，包括：</p>
 * <ul>
 *   <li>查看所有订单列表</li>
 *   <li>订单发货操作</li>
 * </ul>
 *
 * <p>所有接口需要管理员权限（@RequireAdmin 注解）。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "管理员-订单接口")
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * 获取所有订单列表（管理员）
     *
     * @param status 订单状态（可选）
     * @param page   页码（默认 1）
     * @param size   每页大小（默认 10）
     * @return 分页订单列表
     */
    @RequireAdmin
    @Operation(summary = "所有订单列表")
    @GetMapping("/list")
    public R<PageResult<OrderDTO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return R.ok(orderService.listAllOrders(status, page, size));
    }

    /**
     * 订单发货（管理员）
     *
     * <p>将订单状态从已付款改为已发货。</p>
     *
     * @param id 订单 ID
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "发货")
    @PutMapping("/ship/{id}")
    public R<Void> ship(@PathVariable Long id) {
        orderService.shipOrder(id);
        return R.ok();
    }
}
