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

@Tag(name = "轮播图接口")
@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "轮播图列表")
    @GetMapping("/list")
    public R<List<Banner>> list() {
        return R.ok(bannerService.list());
    }
}
