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

/**
 * 订单控制器
 *
 * <p>提供用户的订单操作接口，包括：</p>
 * <ul>
 *   <li>创建订单</li>
 *   <li>查看订单列表和详情</li>
 *   <li>取消订单</li>
 *   <li>模拟支付</li>
 *   <li>确认收货</li>
 *   <li>申请退货</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "订单接口")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     *
     * @param dto 创建订单请求（包含地址 ID 和购物车项 ID 列表）
     * @return 订单详情
     */
    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public R<OrderDTO> createOrder(@RequestBody CreateOrderDTO dto) {
        Long userId = checkLogin();
        return R.ok(orderService.createOrder(userId, dto));
    }

    /**
     * 获取用户的订单列表
     *
     * @param status 订单状态（可选，为空时查询所有状态）
     * @param page   页码（默认 1）
     * @param size   每页大小（默认 10）
     * @return 分页订单列表
     */
    @Operation(summary = "我的订单列表")
    @GetMapping("/list")
    public R<PageResult<OrderDTO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = checkLogin();
        return R.ok(orderService.listOrders(userId, status, page, size));
    }

    /**
     * 获取订单详情
     *
     * @param id 订单 ID
     * @return 订单详情
     */
    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public R<OrderDTO> detail(@PathVariable Long id) {
        Long userId = checkLogin();
        return R.ok(orderService.getOrderDetail(userId, id));
    }

    /**
     * 取消订单
     *
     * <p>只能取消待付款状态的订单，取消后恢复库存。</p>
     *
     * @param id 订单 ID
     * @return 操作结果
     */
    @Operation(summary = "取消订单")
    @PutMapping("/cancel/{id}")
    public R<Void> cancel(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.cancelOrder(userId, id);
        return R.ok();
    }

    /**
     * 模拟支付订单
     *
     * <p>将订单状态从待付款改为已付款，记录支付时间。</p>
     *
     * @param id 订单 ID
     * @return 操作结果
     */
    @Operation(summary = "模拟支付")
    @PostMapping("/pay/{id}")
    public R<Void> pay(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.payOrder(userId, id);
        return R.ok();
    }

    /**
     * 确认收货
     *
     * <p>将订单状态从已发货改为已完成。</p>
     *
     * @param id 订单 ID
     * @return 操作结果
     */
    @Operation(summary = "确认收货")
    @PutMapping("/confirm/{id}")
    public R<Void> confirm(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.confirmOrder(userId, id);
        return R.ok();
    }

    /**
     * 申请退货
     *
     * <p>只能对已付款或已发货的订单申请退货，退货后恢复库存。</p>
     *
     * @param id 订单 ID
     * @return 操作结果
     */
    @Operation(summary = "申请退货")
    @PutMapping("/return/{id}")
    public R<Void> applyReturn(@PathVariable Long id) {
        Long userId = checkLogin();
        orderService.applyReturn(userId, id);
        return R.ok();
    }

    /**
     * 检查用户是否已登录
     *
     * @return 用户 ID
     * @throws BusinessException 未登录时抛出 401 异常
     */
    private Long checkLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}
