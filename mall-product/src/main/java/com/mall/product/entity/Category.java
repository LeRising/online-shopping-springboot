package com.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类实体类
 *
 * <p>对应数据库 category 表，存储商品分类信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("category")
public class Category extends BaseEntity {

    /** 分类名称 */
    private String name;

    /** 父分类 ID（0 表示顶级分类） */
    private Long parentId;

    /** 排序值（越小越靠前） */
    private Integer sort;

    /** 分类图标 */
    private String icon;
}
