package com.mall.product.feign;

import com.mall.common.result.R;
import com.mall.product.dto.ProductDTO;
import com.mall.product.dto.ProductSimpleDTO;
import com.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 商品 Feign 服务接口
 *
 * <p>供其他微服务通过 Feign 调用的商品接口，包括：</p>
 * <ul>
 *   <li>获取商品简略信息</li>
 *   <li>扣减库存</li>
 *   <li>恢复库存</li>
 *   <li>统计商品数量</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/feign/product")
@RequiredArgsConstructor
public class ProductFeignService {

    private final ProductService productService;

    /**
     * 获取商品简略信息
     *
     * @param id 商品 ID
     * @return 商品简略信息
     */
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

    /**
     * 扣减商品库存
     *
     * @param productId 商品 ID
     * @param quantity  扣减数量
     * @return 操作结果
     */
    @PutMapping("/deduct-stock")
    public R<Void> deductStock(@RequestParam Long productId, @RequestParam int quantity) {
        log.info("收到扣减库存请求: productId={}, quantity={}", productId, quantity);
        productService.deductStock(productId, quantity);
        return R.ok();
    }

    /**
     * 恢复商品库存
     *
     * @param productId 商品 ID
     * @param quantity  恢复数量
     * @return 操作结果
     */
    @PutMapping("/restore-stock")
    public R<Void> restoreStock(@RequestParam Long productId, @RequestParam int quantity) {
        log.info("收到恢复库存请求: productId={}, quantity={}", productId, quantity);
        productService.restoreStock(productId, quantity);
        return R.ok();
    }

    /**
     * 统计商品总数
     *
     * @return 商品数量
     */
    @GetMapping("/count")
    public R<Long> countProducts() {
        return R.ok(productService.count());
    }
}
