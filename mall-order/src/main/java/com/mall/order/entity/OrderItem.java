package com.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单项实体类
 *
 * <p>对应数据库 order_item 表，存储订单中的商品明细信息。</p>
 *
 * <p>每个订单可以包含多个订单项，每个订单项对应一个商品。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_item")
public class OrderItem extends BaseEntity {

    /** 订单 ID */
    private Long orderId;

    /** 商品 ID */
    private Long productId;

    /** 商品名称（下单时快照） */
    private String productName;

    /** 商品图片（下单时快照） */
    private String productImage;

    /** 商品单价（下单时快照） */
    private BigDecimal price;

    /** 购买数量 */
    private Integer quantity;
}
