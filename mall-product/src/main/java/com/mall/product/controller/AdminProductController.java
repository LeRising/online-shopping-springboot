package com.mall.product.controller;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.product.dto.ProductDTO;
import com.mall.product.entity.Product;
import com.mall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员商品控制器
 *
 * <p>提供管理员的商品管理接口，包括：</p>
 * <ul>
 *   <li>查看商品分页列表</li>
 *   <li>新增商品</li>
 *   <li>修改商品</li>
 *   <li>删除商品</li>
 * </ul>
 *
 * <p>所有接口需要管理员权限（@RequireAdmin 注解）。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "后台商品管理接口")
@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /**
     * 获取商品分页列表
     *
     * @param page       页码（默认 1）
     * @param size       每页大小（默认 10）
     * @param categoryId 分类 ID（可选）
     * @param keyword    搜索关键字（可选）
     * @return 分页商品列表
     */
    @RequireAdmin
    @Operation(summary = "商品分页列表")
    @GetMapping("/list")
    public R<PageResult<ProductDTO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        return R.ok(productService.list(page, size, categoryId, keyword));
    }

    /**
     * 新增商品
     *
     * @param product 商品信息
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "新增商品")
    @PostMapping
    public R<Void> save(@RequestBody Product product) {
        productService.save(product);
        return R.ok();
    }

    /**
     * 修改商品
     *
     * @param product 商品信息
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "修改商品")
    @PutMapping
    public R<Void> update(@RequestBody Product product) {
        productService.update(product);
        return R.ok();
    }

    /**
     * 删除商品
     *
     * @param id 商品 ID
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return R.ok();
    }
}
