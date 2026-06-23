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

@Tag(name = "后台分类管理接口")
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @RequireAdmin
    @Operation(summary = "分类列表")
    @GetMapping("/list")
    public R<List<Category>> list() {
        return R.ok(categoryService.list());
    }

    @RequireAdmin
    @Operation(summary = "新增分类")
    @PostMapping
    public R<Void> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.ok();
    }

    @RequireAdmin
    @Operation(summary = "修改分类")
    @PutMapping
    public R<Void> update(@RequestBody Category category) {
        categoryService.update(category);
        return R.ok();
    }

    @RequireAdmin
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return R.ok();
    }
}
