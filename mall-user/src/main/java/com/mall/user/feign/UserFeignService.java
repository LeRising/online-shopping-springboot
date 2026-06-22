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
 * Feign服务接口，供其他微服务内部调用
 */
@RestController
@RequestMapping("/feign/user")
@RequiredArgsConstructor
public class UserFeignService {

    private final UserService userService;
    private final UserAddressService userAddressService;

    @GetMapping("/{userId}/addresses")
    public R<List<UserAddress>> getAddresses(@PathVariable Long userId) {
        return R.ok(userAddressService.listByUserId(userId));
    }

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

    @GetMapping("/count")
    public R<Long> countUsers() {
        return R.ok(userService.count());
    }
}
