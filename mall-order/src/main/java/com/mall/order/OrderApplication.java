package com.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 订单服务启动类
 *
 * <p>订单服务模块，负责购物车管理和订单生命周期管理。</p>
 *
 * <p>功能包括：</p>
 * <ul>
 *   <li>购物车管理（添加、修改、删除、选中状态）</li>
 *   <li>订单创建（从购物车下单、库存扣减、地址快照）</li>
 *   <li>订单状态流转（待付款→已付款→已发货→已完成，支持取消和退货）</li>
 *   <li>管理员订单管理（查看所有订单、发货）</li>
 *   <li>仪表盘统计（订单数、商品数、用户数）</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.mall.order", "com.mall.common"})
@MapperScan("com.mall.order.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
