package com.mall.order.service;

import com.mall.common.result.PageResult;
import com.mall.order.dto.CreateOrderDTO;
import com.mall.order.dto.OrderDTO;

/**
 * 订单服务接口
 *
 * <p>提供订单的完整生命周期管理，包括创建、支付、发货、确认、退货等操作。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * <p>从购物车中选中的商品创建订单，包含以下步骤：</p>
     * <ol>
     *   <li>获取购物车中的商品</li>
     *   <li>通过 Feign 获取商品信息</li>
     *   <li>扣减库存</li>
     *   <li>获取收货地址快照</li>
     *   <li>创建订单和订单项</li>
     *   <li>清空已下单的购物车项</li>
     * </ol>
     *
     * @param userId 用户 ID
     * @param dto    创建订单请求（包含地址 ID 和购物车项 ID 列表）
     * @return 订单详情
     */
    OrderDTO createOrder(Long userId, CreateOrderDTO dto);

    /**
     * 查询用户的订单列表
     *
     * @param userId 用户 ID
     * @param status 订单状态（可选，为空时查询所有状态）
     * @param page   页码
     * @param size   每页大小
     * @return 分页订单列表
     */
    PageResult<OrderDTO> listOrders(Long userId, Integer status, int page, int size);

    /**
     * 获取订单详情
     *
     * @param userId  用户 ID
     * @param orderId 订单 ID
     * @return 订单详情
     */
    OrderDTO getOrderDetail(Long userId, Long orderId);

    /**
     * 取消订单
     *
     * <p>只能取消待付款状态的订单，取消后恢复库存。</p>
     *
     * @param userId  用户 ID
     * @param orderId 订单 ID
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 模拟支付订单
     *
     * <p>将订单状态从待付款改为已付款，记录支付时间。</p>
     *
     * @param userId  用户 ID
     * @param orderId 订单 ID
     */
    void payOrder(Long userId, Long orderId);

    /**
     * 确认收货
     *
     * <p>将订单状态从已发货改为已完成。</p>
     *
     * @param userId  用户 ID
     * @param orderId 订单 ID
     */
    void confirmOrder(Long userId, Long orderId);

    /**
     * 申请退货
     *
     * <p>只能对已付款或已发货的订单申请退货，退货后恢复库存。</p>
     *
     * @param userId  用户 ID
     * @param orderId 订单 ID
     */
    void applyReturn(Long userId, Long orderId);

    /**
     * 查询所有订单（管理员）
     *
     * @param status 订单状态（可选）
     * @param page   页码
     * @param size   每页大小
     * @return 分页订单列表
     */
    PageResult<OrderDTO> listAllOrders(Integer status, int page, int size);

    /**
     * 订单发货（管理员）
     *
     * <p>将订单状态从已付款改为已发货。</p>
     *
     * @param orderId 订单 ID
     */
    void shipOrder(Long orderId);
}
