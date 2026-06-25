package com.mall.order.feign;

import com.mall.common.result.R;
import com.mall.order.dto.ProductSimpleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 商品服务 Feign 客户端
 *
 * <p>用于调用 mall-product 服务的内部接口，支持：</p>
 * <ul>
 *   <li>查询商品信息</li>
 *   <li>扣减库存（创建订单时）</li>
 *   <li>恢复库存（取消订单/退货时）</li>
 *   <li>统计商品数量（仪表盘）</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@FeignClient(name = "mall-product")
public interface ProductFeignClient {

    /**
     * 获取商品简略信息
     *
     * @param id 商品 ID
     * @return 商品信息
     */
    @GetMapping("/feign/product/{id}")
    R<ProductSimpleDTO> getProduct(@PathVariable("id") Long id);

    /**
     * 扣减商品库存
     *
     * @param productId 商品 ID
     * @param quantity  扣减数量
     * @return 操作结果
     */
    @PutMapping("/feign/product/deduct-stock")
    R<Void> deductStock(@RequestParam("productId") Long productId,
                        @RequestParam("quantity") int quantity);

    /**
     * 恢复商品库存
     *
     * @param productId 商品 ID
     * @param quantity  恢复数量
     * @return 操作结果
     */
    @PutMapping("/feign/product/restore-stock")
    R<Void> restoreStock(@RequestParam("productId") Long productId,
                         @RequestParam("quantity") int quantity);

    /**
     * 统计商品总数
     *
     * @return 商品数量
     */
    @GetMapping("/feign/product/count")
    R<Long> countProducts();
}
