package com.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图实体类
 *
 * <p>对应数据库 banner 表，存储首页轮播图信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("banner")
public class Banner extends BaseEntity {

    /** 轮播图图片 URL */
    private String image;

    /** 点击跳转链接 */
    private String url;

    /** 排序值（越小越靠前） */
    private Integer sort;
}
