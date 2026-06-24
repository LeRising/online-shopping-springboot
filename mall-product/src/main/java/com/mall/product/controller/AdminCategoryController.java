package com.mall.product.controller;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.result.R;
import com.mall.product.entity.Category;
import com.mall.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员分类控制器
 *
 * <p>提供管理员的商品分类管理接口，包括：</p>
 * <ul>
 *   <li>查看分类列表</li>
 *   <li>新增分类</li>
 *   <li>修改分类</li>
 *   <li>删除分类</li>
 * </ul>
 *
 * <p>所有接口需要管理员权限（@RequireAdmin 注解）。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "后台分类管理接口")
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    /**
     * 获取分类列表
     *
     * @return 分类列表
     */
    @RequireAdmin
    @Operation(summary = "分类列表")
    @GetMapping("/list")
    public R<List<Category>> list() {
        return R.ok(categoryService.list());
    }

    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "新增分类")
    @PostMapping
    public R<Void> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改分类
     *
     * @param category 分类信息
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "修改分类")
    @PutMapping
    public R<Void> update(@RequestBody Category category) {
        categoryService.update(category);
        return R.ok();
    }

    /**
     * 删除分类
     *
     * @param id 分类 ID
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return R.ok();
    }
}
