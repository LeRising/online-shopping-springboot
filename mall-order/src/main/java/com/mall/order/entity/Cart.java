package com.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cart")
public class Cart extends BaseEntity {
    private Long userId;
    private Long productId;
    private Integer quantity;
    /** 是否选中：0=未选中，1=选中 */
    private Integer checked;
}
