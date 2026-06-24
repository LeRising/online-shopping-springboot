package com.mall.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 收货地址请求 DTO
 *
 * <p>添加或修改收货地址时的请求参数。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class AddressDTO {

    /** 地址 ID（修改时必填） */
    private Long id;

    /** 收货人姓名 */
    @NotBlank(message = "收货人姓名不能为空")
    private String name;

    /** 收货人电话 */
    @NotBlank(message = "收货人电话不能为空")
    private String phone;

    /** 省份 */
    @NotBlank(message = "省份不能为空")
    private String province;

    /** 城市 */
    @NotBlank(message = "城市不能为空")
    private String city;

    /** 区县 */
    @NotBlank(message = "区县不能为空")
    private String district;

    /** 详细地址 */
    @NotBlank(message = "详细地址不能为空")
    private String detail;

    /** 是否默认地址：0-否，1-是 */
    private Integer isDefault;
}
