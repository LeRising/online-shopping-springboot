package com.mall.order.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import com.mall.common.util.UserContext;
import com.mall.order.feign.ProductFeignClient;
import com.mall.order.feign.UserFeignClient;
import com.mall.order.mapper.OrderInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "管理员-仪表盘")
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final OrderInfoMapper orderInfoMapper;
    private final ProductFeignClient productFeignClient;
    private final UserFeignClient userFeignClient;

    @Operation(summary = "统计数据")
    @GetMapping("/stats")
    public R<Map<String, Object>> stats() {
        checkAdmin();
        Map<String, Object> result = new HashMap<>();

        long orderCount = orderInfoMapper.selectCount(null);
        result.put("orderCount", orderCount);

        try {
            R<Long> productResult = productFeignClient.countProducts();
            result.put("productCount", productResult != null && productResult.getCode() == 200
                    ? productResult.getData() : 0);
        } catch (Exception e) {
            log.warn("获取商品数量失败: {}", e.getMessage());
            result.put("productCount", 0);
        }

        try {
            R<Long> userResult = userFeignClient.countUsers();
            result.put("userCount", userResult != null && userResult.getCode() == 200
                    ? userResult.getData() : 0);
        } catch (Exception e) {
            log.warn("获取用户数量失败: {}", e.getMessage());
            result.put("userCount", 0);
        }

        return R.ok(result);
    }

    private void checkAdmin() {
        Integer role = UserContext.getRole();
        if (role == null || role != 1) {
            throw new BusinessException(403, "无管理员权限");
        }
    }
}
