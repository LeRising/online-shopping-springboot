package com.mall.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户收货地址实体类
 *
 * <p>对应数据库 user_address 表，存储用户的收货地址信息。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_address")
public class UserAddress extends BaseEntity {

    /** 用户 ID */
    private Long userId;

    /** 收货人姓名 */
    private String name;

    /** 收货人电话 */
    private String phone;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 区县 */
    private String district;

    /** 详细地址 */
    private String detail;

    /** 是否默认地址：0-否，1-是 */
    private Integer isDefault;
}
