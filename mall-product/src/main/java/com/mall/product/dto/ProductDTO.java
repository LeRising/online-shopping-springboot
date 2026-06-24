package com.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品详情 DTO
 *
 * <p>用于返回商品的完整信息，包含分类名称。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品 ID */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品描述 */
    private String description;

    /** 商品价格 */
    private BigDecimal price;

    /** 商品原价 */
    private BigDecimal originalPrice;

    /** 库存数量 */
    private Integer stock;

    /** 分类 ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 商品主图 URL */
    private String image;

    /** 商品图片列表（JSON 格式） */
    private String images;

    /** 商品状态：0-下架，1-上架 */
    private Integer status;

    /** 销量 */
    private Integer sales;
}
