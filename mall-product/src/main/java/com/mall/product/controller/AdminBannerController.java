package com.mall.product.controller;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.result.R;
import com.mall.product.entity.Banner;
import com.mall.product.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台轮播图管理接口")
@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @RequireAdmin
    @Operation(summary = "轮播图列表")
    @GetMapping("/list")
    public R<List<Banner>> list() {
        return R.ok(bannerService.list());
    }

    @RequireAdmin
    @Operation(summary = "新增轮播图")
    @PostMapping
    public R<Void> save(@RequestBody Banner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    @RequireAdmin
    @Operation(summary = "修改轮播图")
    @PutMapping
    public R<Void> update(@RequestBody Banner banner) {
        bannerService.update(banner);
        return R.ok();
    }

    @RequireAdmin
    @Operation(summary = "删除轮播图")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return R.ok();
    }
}
