package com.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_info")
public class OrderInfo extends BaseEntity {
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    /** 状态：0=待付款，1=已付款，2=已发货，3=已完成，4=已取消，5=已退货 */
    private Integer status;
    /** 支付方式：0=微信，1=支付宝，2=云闪付 */
    private Integer payMethod;
    /** 收货地址快照JSON */
    private String addressSnapshot;
    private LocalDateTime payTime;
}
