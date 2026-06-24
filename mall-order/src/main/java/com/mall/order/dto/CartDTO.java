package com.mall.order.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 购物车展示 DTO
 *
 * <p>用于返回购物车列表时的数据传输，包含商品信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class CartDTO {

    /** 购物车项 ID */
    private Long id;

    /** 商品 ID */
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 商品图片 */
    private String productImage;

    /** 商品单价 */
    private BigDecimal price;

    /** 商品库存 */
    private Integer stock;

    /** 购买数量 */
    private Integer quantity;

    /** 是否选中：0=未选中，1=选中 */
    private Integer checked;
}
