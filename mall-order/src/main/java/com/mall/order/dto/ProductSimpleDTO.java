package com.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品简略信息 DTO
 *
 * <p>用于 Feign 接口返回的商品信息，包含商品的基本字段。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class ProductSimpleDTO {

    /** 商品 ID */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品图片 */
    private String image;

    /** 商品价格 */
    private BigDecimal price;

    /** 商品库存 */
    private Integer stock;
}
