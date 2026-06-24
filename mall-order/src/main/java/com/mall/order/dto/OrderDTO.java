package com.mall.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情 DTO
 *
 * <p>用于返回订单详细信息，包含订单状态文本和订单项列表。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class OrderDTO {

    /** 订单 ID */
    private Long id;

    /** 订单编号 */
    private String orderNo;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 订单状态码 */
    private Integer status;

    /** 订单状态文本（待付款/已付款/已发货/已完成/已取消/已退货） */
    private String statusText;

    /** 收货地址快照 */
    private String addressSnapshot;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 订单项列表 */
    private List<OrderItemDTO> items;

    /**
     * 根据状态码设置状态文本
     */
    public void setStatusText() {
        String[] texts = {"待付款", "已付款", "已发货", "已完成", "已取消", "已退货"};
        this.statusText = (status != null && status >= 0 && status < texts.length) ? texts[status] : "未知";
    }
}
