package com.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购物车实体类
 *
 * <p>对应数据库 cart 表，存储用户的购物车信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cart")
public class Cart extends BaseEntity {

    /** 用户 ID */
    private Long userId;

    /** 商品 ID */
    private Long productId;

    /** 商品数量 */
    private Integer quantity;

    /** 是否选中：0=未选中，1=选中 */
    private Integer checked;
}
