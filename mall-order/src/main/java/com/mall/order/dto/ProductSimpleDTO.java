package com.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品简略信息 DTO，用于 Feign 接口返回
 */
@Data
public class ProductSimpleDTO {

    private Long id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer stock;
}
