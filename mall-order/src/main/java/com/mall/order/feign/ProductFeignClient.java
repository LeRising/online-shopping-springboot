package com.mall.order.feign;

import com.mall.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商品服务 Feign 客户端（简化版，无熔断降级）
 */
@FeignClient(name = "mall-product", url = "http://localhost:8082")
public interface ProductFeignClient {

    @GetMapping("/feign/product/{id}")
    R<Map<String, Object>> getProduct(@PathVariable("id") Long id);

    @PutMapping("/feign/product/deduct-stock")
    R<Void> deductStock(@RequestParam("productId") Long productId,
                        @RequestParam("quantity") int quantity);

    @PutMapping("/feign/product/restore-stock")
    R<Void> restoreStock(@RequestParam("productId") Long productId,
                         @RequestParam("quantity") int quantity);

    @GetMapping("/feign/product/count")
    R<Long> countProducts();
}
