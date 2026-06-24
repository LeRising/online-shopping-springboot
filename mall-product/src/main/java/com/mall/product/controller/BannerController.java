package com.mall.product.controller;

import com.mall.common.result.R;
import com.mall.product.entity.Banner;
import com.mall.product.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 轮播图控制器
 *
 * <p>提供前台轮播图的查询接口。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Tag(name = "轮播图接口")
@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    /**
     * 获取轮播图列表
     *
     * @return 轮播图列表（按排序值升序）
     */
    @Operation(summary = "轮播图列表")
    @GetMapping("/list")
    public R<List<Banner>> list() {
        return R.ok(bannerService.list());
    }
}
