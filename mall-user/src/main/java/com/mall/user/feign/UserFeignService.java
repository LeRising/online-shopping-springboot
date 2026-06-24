package com.mall.user.feign;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.user.entity.UserAddress;
import com.mall.user.service.UserAddressService;
import com.mall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 Feign 服务接口
 *
 * <p>供其他微服务通过 Feign 调用的用户接口，包括：</p>
 * <ul>
 *   <li>获取用户收货地址列表</li>
 *   <li>获取单个收货地址</li>
 *   <li>统计用户数量</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@RestController
@RequestMapping("/feign/user")
@RequiredArgsConstructor
public class UserFeignService {

    private final UserService userService;
    private final UserAddressService userAddressService;

    /**
     * 获取用户的所有收货地址
     *
     * @param userId 用户 ID
     * @return 地址列表
     */
    @GetMapping("/{userId}/addresses")
    public R<List<UserAddress>> getAddresses(@PathVariable Long userId) {
        return R.ok(userAddressService.listByUserId(userId));
    }

    /**
     * 获取用户的单个收货地址
     *
     * @param userId    用户 ID
     * @param addressId 地址 ID
     * @return 地址信息（Map 格式）
     */
    @GetMapping("/{userId}/address/{addressId}")
    public R<Map<String, Object>> getAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        List<UserAddress> addresses = userAddressService.listByUserId(userId);
        UserAddress addr = addresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("地址不存在"));
        Map<String, Object> map = new HashMap<>();
        map.put("id", addr.getId());
        map.put("name", addr.getName());
        map.put("phone", addr.getPhone());
        map.put("province", addr.getProvince());
        map.put("city", addr.getCity());
        map.put("district", addr.getDistrict());
        map.put("detail", addr.getDetail());
        return R.ok(map);
    }

    /**
     * 统计用户总数
     *
     * @return 用户数量
     */
    @GetMapping("/count")
    public R<Long> countUsers() {
        return R.ok(userService.count());
    }
}
