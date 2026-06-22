package com.mall.product.feign;

import com.mall.common.result.R;
import com.mall.common.exception.BusinessException;
import com.mall.product.dto.ProductDTO;
import com.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 供其他服务通过 Feign 调用的商品接口
 */
@Slf4j
@RestController
@RequestMapping("/feign/product")
@RequiredArgsConstructor
public class ProductFeignService {

    private final ProductService productService;

    @GetMapping("/{id}")
    public R<Map<String, Object>> getProduct(@PathVariable Long id) {
        ProductDTO product = productService.getDetail(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", product.getId());
        map.put("name", product.getName());
        map.put("price", product.getPrice());
        map.put("stock", product.getStock());
        map.put("image", product.getImage());
        return R.ok(map);
    }

    @PutMapping("/deduct-stock")
    public R<Void> deductStock(@RequestParam Long productId, @RequestParam int quantity) {
        log.info("收到扣减库存请求: productId={}, quantity={}", productId, quantity);
        productService.deductStock(productId, quantity);
        return R.ok();
    }

    @PutMapping("/restore-stock")
    public R<Void> restoreStock(@RequestParam Long productId, @RequestParam int quantity) {
        log.info("收到恢复库存请求: productId={}, quantity={}", productId, quantity);
        productService.restoreStock(productId, quantity);
        return R.ok();
    }

    @GetMapping("/count")
    public R<Long> countProducts() {
        return R.ok(productService.count());
    }
}
