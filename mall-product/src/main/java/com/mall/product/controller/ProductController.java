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

/**
 * 商品控制器
 *
 * <p>提供前台商品的查询接口，包括：</p>
 * <ul>
 *   <li>商品分页列表（支持分类筛选和关键字搜索）</li>
 *   <li>商品详情</li>
 *   <li>热门商品</li>
 *   <li>新品推荐</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "商品接口")
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

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
     * 获取商品详情
     *
     * @param id 商品 ID
     * @return 商品详情
     */
    @Operation(summary = "商品详情")
    @GetMapping("/{id}")
    public R<ProductDTO> detail(@PathVariable Long id) {
        return R.ok(productService.getDetail(id));
    }

    /**
     * 获取热门商品
     *
     * @return 热门商品列表（按销量排序，取前 8 个）
     */
    @Operation(summary = "热门商品")
    @GetMapping("/hot")
    public R<List<ProductDTO>> hot() {
        return R.ok(productService.getHotProducts());
    }

    /**
     * 获取新品推荐
     *
     * @return 新品列表（按创建时间排序，取前 8 个）
     */
    @Operation(summary = "新品推荐")
    @GetMapping("/new")
    public R<List<ProductDTO>> newProducts() {
        return R.ok(productService.getNewProducts());
    }
}
