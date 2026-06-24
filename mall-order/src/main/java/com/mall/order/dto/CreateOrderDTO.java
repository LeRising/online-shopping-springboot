package com.mall.order.dto;

import lombok.Data;
import java.util.List;

/**
 * 创建订单请求 DTO
 *
 * <p>用户提交订单时的请求参数，包含收货地址和购物车项。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class CreateOrderDTO {

    /** 收货地址 ID（为空时使用默认地址） */
    private Long addressId;

    /** 购物车项 ID 列表（为空时使用所有选中的购物车项） */
    private List<Long> cartIds;
}
