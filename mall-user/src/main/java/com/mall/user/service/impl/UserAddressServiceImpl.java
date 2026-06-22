package com.mall.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.user.dto.AddressDTO;
import com.mall.user.entity.UserAddress;
import com.mall.user.mapper.UserAddressMapper;
import com.mall.user.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> listByUserId(Long userId) {
        return userAddressMapper.selectList(
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .orderByDesc(UserAddress::getIsDefault)
                        .orderByDesc(UserAddress::getUpdateTime));
    }

    @Override
    @Transactional
    public void addAddress(Long userId, AddressDTO dto) {
        // 如果设为默认地址，先取消其他默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            cancelDefaultAddress(userId);
        }

        UserAddress address = new UserAddress();
        BeanUtil.copyProperties(dto, address);
        address.setUserId(userId);
        if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }

        userAddressMapper.insert(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long userId, AddressDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("地址ID不能为空");
        }

        // 校验地址是否属于当前用户
        UserAddress existing = userAddressMapper.selectById(dto.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权修改");
        }

        // 如果设为默认地址，先取消其他默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            cancelDefaultAddress(userId);
        }

        UserAddress address = new UserAddress();
        BeanUtil.copyProperties(dto, address);
        address.setUserId(userId);

        userAddressMapper.updateById(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress existing = userAddressMapper.selectById(addressId);
        if (existing == null || !existing.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权删除");
        }

        userAddressMapper.deleteById(addressId);
    }

    /**
     * 取消用户的默认地址
     */
    private void cancelDefaultAddress(Long userId) {
        userAddressMapper.update(null,
                new LambdaUpdateWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .eq(UserAddress::getIsDefault, 1)
                        .set(UserAddress::getIsDefault, 0));
    }
}
