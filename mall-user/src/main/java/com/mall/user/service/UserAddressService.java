package com.mall.user.service;

import com.mall.user.dto.AddressDTO;
import com.mall.user.entity.UserAddress;

import java.util.List;

public interface UserAddressService {

    /**
     * 获取用户地址列表
     */
    List<UserAddress> listByUserId(Long userId);

    /**
     * 添加地址
     */
    void addAddress(Long userId, AddressDTO dto);

    /**
     * 更新地址
     */
    void updateAddress(Long userId, AddressDTO dto);

    /**
     * 删除地址
     */
    void deleteAddress(Long userId, Long addressId);
}
