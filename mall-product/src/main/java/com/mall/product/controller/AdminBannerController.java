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

/**
 * 管理员轮播图控制器
 *
 * <p>提供管理员的轮播图管理接口，包括：</p>
 * <ul>
 *   <li>查看轮播图列表</li>
 *   <li>新增轮播图</li>
 *   <li>修改轮播图</li>
 *   <li>删除轮播图</li>
 * </ul>
 *
 * <p>所有接口需要管理员权限（@RequireAdmin 注解）。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "后台轮播图管理接口")
@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    /**
     * 获取轮播图列表
     *
     * @return 轮播图列表
     */
    @RequireAdmin
    @Operation(summary = "轮播图列表")
    @GetMapping("/list")
    public R<List<Banner>> list() {
        return R.ok(bannerService.list());
    }

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "新增轮播图")
    @PostMapping
    public R<Void> save(@RequestBody Banner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    /**
     * 修改轮播图
     *
     * @param banner 轮播图信息
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "修改轮播图")
    @PutMapping
    public R<Void> update(@RequestBody Banner banner) {
        bannerService.update(banner);
        return R.ok();
    }

    /**
     * 删除轮播图
     *
     * @param id 轮播图 ID
     * @return 操作结果
     */
    @RequireAdmin
    @Operation(summary = "删除轮播图")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return R.ok();
    }
}
