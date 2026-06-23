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

@Tag(name = "后台商品管理接口")
@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

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

    @RequireAdmin
    @Operation(summary = "新增商品")
    @PostMapping
    public R<Void> save(@RequestBody Product product) {
        productService.save(product);
        return R.ok();
    }

    @RequireAdmin
    @Operation(summary = "修改商品")
    @PutMapping
    public R<Void> update(@RequestBody Product product) {
        productService.update(product);
        return R.ok();
    }

    @RequireAdmin
    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return R.ok();
    }
}
