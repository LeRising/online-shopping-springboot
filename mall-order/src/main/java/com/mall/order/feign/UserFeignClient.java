package com.mall.order.feign;

import com.mall.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户服务 Feign 客户端（简化版，无熔断降级）
 */
@FeignClient(name = "mall-user", url = "http://localhost:8081")
public interface UserFeignClient {

    @GetMapping("/feign/user/{userId}/address/{addressId}")
    R<Map<String, Object>> getAddress(@PathVariable("userId") Long userId,
                                      @PathVariable("addressId") Long addressId);

    @GetMapping("/feign/user/count")
    R<Long> countUsers();
}
