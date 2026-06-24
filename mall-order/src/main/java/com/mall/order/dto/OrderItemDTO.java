package com.mall.order.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单项 DTO
 *
 * <p>用于返回订单中单个商品的详细信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class OrderItemDTO {

    /** 商品 ID */
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 商品图片 */
    private String productImage;

    /** 商品单价 */
    private BigDecimal price;

    /** 购买数量 */
    private Integer quantity;
}
