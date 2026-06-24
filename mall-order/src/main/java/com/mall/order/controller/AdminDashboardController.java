package com.mall.order.controller;

import com.mall.common.annotation.RequireAdmin;
import com.mall.common.result.R;
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

/**
 * 管理员仪表盘控制器
 *
 * <p>提供管理员后台的统计数据接口，包括：</p>
 * <ul>
 *   <li>订单总数</li>
 *   <li>商品总数</li>
 *   <li>用户总数</li>
 * </ul>
 *
 * <p>所有接口需要管理员权限（@RequireAdmin 注解）。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "管理员-仪表盘")
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final OrderInfoMapper orderInfoMapper;
    private final ProductFeignClient productFeignClient;
    private final UserFeignClient userFeignClient;

    /**
     * 获取统计数据
     *
     * <p>通过 Feign 调用商品服务和用户服务获取统计数据，失败时返回 0。</p>
     *
     * @return 统计数据（包含 orderCount、productCount、userCount）
     */
    @RequireAdmin
    @Operation(summary = "统计数据")
    @GetMapping("/stats")
    public R<Map<String, Object>> stats() {
        Map<String, Object> result = new HashMap<>();

        // 获取订单总数
        long orderCount = orderInfoMapper.selectCount(null);
        result.put("orderCount", orderCount);

        // 获取商品总数
        try {
            R<Long> productResult = productFeignClient.countProducts();
            result.put("productCount", productResult != null && productResult.getCode() == 200
                    ? productResult.getData() : 0);
        } catch (Exception e) {
            log.warn("获取商品数量失败: {}", e.getMessage());
            result.put("productCount", 0);
        }

        // 获取用户总数
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
}
