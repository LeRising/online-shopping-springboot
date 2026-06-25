package com.mall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 *
 * <p>用户服务模块，负责用户注册、登录、信息管理、收货地址管理等功能。</p>
 *
 * <p>功能包括：</p>
 * <ul>
 *   <li>用户注册（BCrypt 密码加密）</li>
 *   <li>用户登录（Token 认证）</li>
 *   <li>用户信息管理（查看、修改）</li>
 *   <li>收货地址管理（增删改查、默认地址）</li>
 *   <li>Token 验证（供 Gateway 调用）</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.mall.user", "com.mall.common"})
@MapperScan("com.mall.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
