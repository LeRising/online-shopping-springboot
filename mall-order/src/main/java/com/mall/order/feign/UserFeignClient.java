package com.mall.order.feign;

import com.mall.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户服务 Feign 客户端
 *
 * <p>用于调用 mall-user 服务的内部接口，支持：</p>
 * <ul>
 *   <li>查询用户收货地址列表</li>
 *   <li>查询单个收货地址</li>
 *   <li>统计用户数量（仪表盘）</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@FeignClient(name = "mall-user")
public interface UserFeignClient {

    /**
     * 获取用户的所有收货地址
     *
     * @param userId 用户 ID
     * @return 地址列表
     */
    @GetMapping("/feign/user/{userId}/addresses")
    R<List<Map<String, Object>>> getAddresses(@PathVariable("userId") Long userId);

    /**
     * 获取用户的单个收货地址
     *
     * @param userId    用户 ID
     * @param addressId 地址 ID
     * @return 地址信息
     */
    @GetMapping("/feign/user/{userId}/address/{addressId}")
    R<Map<String, Object>> getAddress(@PathVariable("userId") Long userId,
                                      @PathVariable("addressId") Long addressId);

    /**
     * 统计用户总数
     *
     * @return 用户数量
     */
    @GetMapping("/feign/user/count")
    R<Long> countUsers();
}
