package com.mall.product.feign;

import com.mall.common.result.R;
import com.mall.product.dto.ProductDTO;
import com.mall.product.dto.ProductSimpleDTO;
import com.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public R<ProductSimpleDTO> getProduct(@PathVariable Long id) {
        ProductDTO product = productService.getDetail(id);
        ProductSimpleDTO simpleDTO = new ProductSimpleDTO();
        simpleDTO.setId(product.getId());
        simpleDTO.setName(product.getName());
        simpleDTO.setImage(product.getImage());
        simpleDTO.setPrice(product.getPrice());
        simpleDTO.setStock(product.getStock());
        return R.ok(simpleDTO);
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
