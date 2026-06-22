package com.mall.product.controller;

import com.mall.common.result.PageResult;
import com.mall.common.result.R;
import com.mall.product.dto.ProductDTO;
import com.mall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品接口")
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "商品分页列表")
    @GetMapping("/list")
    public R<PageResult<ProductDTO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        return R.ok(productService.list(page, size, categoryId, keyword));
    }

    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public R<ProductDTO> detail(@PathVariable Long id) {
        return R.ok(productService.getDetail(id));
    }

    @Operation(summary = "热门商品")
    @GetMapping("/hot")
    public R<List<ProductDTO>> hot() {
        return R.ok(productService.getHotProducts());
    }

    @Operation(summary = "新品推荐")
    @GetMapping("/new")
    public R<List<ProductDTO>> newProducts() {
        return R.ok(productService.getNewProducts());
    }
}
