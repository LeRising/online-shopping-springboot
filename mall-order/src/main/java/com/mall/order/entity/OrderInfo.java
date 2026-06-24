package com.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 *
 * <p>对应数据库 order_info 表，存储订单的主要信息。</p>
 *
 * <p>订单状态流转：</p>
 * <pre>
 * 待付款(0) → 已付款(1) → 已发货(2) → 已完成(3)
 *    ↓           ↓
 * 已取消(4)   已退货(5)
 * </pre>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_info")
public class OrderInfo extends BaseEntity {

    /** 订单编号（雪花算法生成） */
    private String orderNo;

    /** 用户 ID */
    private Long userId;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 订单状态：0=待付款，1=已付款，2=已发货，3=已完成，4=已取消，5=已退货 */
    private Integer status;

    /** 收货地址快照（JSON 格式，下单时保存） */
    private String addressSnapshot;

    /** 支付时间 */
    private LocalDateTime payTime;
}
