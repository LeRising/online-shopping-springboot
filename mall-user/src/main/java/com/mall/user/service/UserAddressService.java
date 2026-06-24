package com.mall.user.service;

import com.mall.user.dto.AddressDTO;
import com.mall.user.entity.UserAddress;

import java.util.List;

/**
 * 用户收货地址服务接口
 *
 * <p>提供用户收货地址的增删改查功能。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface UserAddressService {

    /**
     * 获取用户的收货地址列表
     *
     * @param userId 用户 ID
     * @return 地址列表（默认地址优先）
     */
    List<UserAddress> listByUserId(Long userId);

    /**
     * 添加收货地址
     *
     * @param userId 用户 ID
     * @param dto    地址信息
     */
    void addAddress(Long userId, AddressDTO dto);

    /**
     * 更新收货地址
     *
     * @param userId 用户 ID
     * @param dto    地址信息（包含地址 ID）
     */
    void updateAddress(Long userId, AddressDTO dto);

    /**
     * 删除收货地址
     *
     * @param userId    用户 ID
     * @param addressId 地址 ID
     */
    void deleteAddress(Long userId, Long addressId);
}
