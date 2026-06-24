package com.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体类
 *
 * <p>对应数据库 product 表，存储商品信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity {

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

    /** 商品主图 URL */
    private String image;

    /** 商品图片列表（JSON 格式，多个 URL 用逗号分隔） */
    private String images;

    /** 商品状态：0-下架，1-上架 */
    private Integer status;

    /** 销量 */
    private Integer sales;
}
