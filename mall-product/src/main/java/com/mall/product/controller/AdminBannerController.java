package com.mall.product.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
import com.mall.product.entity.Banner;
import com.mall.product.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台轮播图管理接口")
@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @Operation(summary = "轮播图列表")
    @GetMapping("/list")
    public R<List<Banner>> list(HttpSession session) {
        checkAdmin(session);
        return R.ok(bannerService.list());
    }

    @Operation(summary = "新增轮播图")
    @PostMapping
    public R<Void> save(HttpSession session, @RequestBody Banner banner) {
        checkAdmin(session);
        bannerService.save(banner);
        return R.ok();
    }

    @Operation(summary = "修改轮播图")
    @PutMapping
    public R<Void> update(HttpSession session, @RequestBody Banner banner) {
        checkAdmin(session);
        bannerService.update(banner);
        return R.ok();
    }

    @Operation(summary = "删除轮播图")
    @DeleteMapping("/{id}")
    public R<Void> delete(HttpSession session, @PathVariable Long id) {
        checkAdmin(session);
        bannerService.delete(id);
        return R.ok();
    }

    private void checkAdmin(HttpSession session) {
        Integer role = UserContext.getRole(session);
        if (role == null || role != 1) {
            throw new BusinessException(403, "无管理员权限");
        }
    }
}
