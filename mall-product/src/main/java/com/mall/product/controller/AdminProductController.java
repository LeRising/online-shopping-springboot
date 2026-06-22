package com.mall.product.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
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

    @Operation(summary = "商品分页列表")
    @GetMapping("/list")
    public R<PageResult<ProductDTO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        checkAdmin();
        return R.ok(productService.list(page, size, categoryId, keyword));
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public R<Void> save(@RequestBody Product product) {
        checkAdmin();
        productService.save(product);
        return R.ok();
    }

    @Operation(summary = "修改商品")
    @PutMapping
    public R<Void> update(@RequestBody Product product) {
        checkAdmin();
        productService.update(product);
        return R.ok();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        checkAdmin();
        productService.delete(id);
        return R.ok();
    }

    private void checkAdmin() {
        Integer role = UserContext.getRole();
        if (role == null || role != 1) {
            throw new BusinessException(403, "无管理员权限");
        }
    }
}
