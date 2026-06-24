package com.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品服务启动类
 *
 * <p>商品服务模块，负责商品、分类、轮播图的管理。</p>
 *
 * <p>功能包括：</p>
 * <ul>
 *   <li>商品管理（增删改查、分页、热门商品、新品推荐）</li>
 *   <li>分类管理（增删改查）</li>
 *   <li>轮播图管理（增删改查）</li>
 *   <li>库存管理（扣减、恢复，支持 Feign 调用）</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.mall.product", "com.mall.common"})
@MapperScan("com.mall.product.mapper")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
