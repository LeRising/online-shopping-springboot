package com.mall.product.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
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

    @Operation(summary = "分类列表")
    @GetMapping("/list")
    public R<List<Category>> list() {
        checkAdmin();
        return R.ok(categoryService.list());
    }

    @Operation(summary = "新增分类")
    @PostMapping
    public R<Void> save(@RequestBody Category category) {
        checkAdmin();
        categoryService.save(category);
        return R.ok();
    }

    @Operation(summary = "修改分类")
    @PutMapping
    public R<Void> update(@RequestBody Category category) {
        checkAdmin();
        categoryService.update(category);
        return R.ok();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        checkAdmin();
        categoryService.delete(id);
        return R.ok();
    }

    private void checkAdmin() {
        Integer role = UserContext.getRole();
        if (role == null || role != 1) {
            throw new BusinessException(403, "无管理员权限");
        }
    }
}
