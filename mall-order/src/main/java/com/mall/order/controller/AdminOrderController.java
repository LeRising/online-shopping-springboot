package com.mall.order.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
import com.mall.order.dto.OrderDTO;
import com.mall.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员-订单接口")
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "所有订单列表")
    @GetMapping("/list")
    public R<PageResult<OrderDTO>> list(HttpSession session,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        checkAdmin(session);
        return R.ok(orderService.listAllOrders(status, page, size));
    }

    @Operation(summary = "发货")
    @PutMapping("/ship/{id}")
    public R<Void> ship(HttpSession session, @PathVariable Long id) {
        checkAdmin(session);
        orderService.shipOrder(id);
        return R.ok();
    }

    private void checkAdmin(HttpSession session) {
        Integer role = UserContext.getRole(session);
        if (role == null || role != 1) {
            throw new BusinessException(403, "无权限，需要管理员身份");
        }
    }
}
