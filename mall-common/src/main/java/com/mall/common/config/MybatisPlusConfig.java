package com.mall.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 配置类
 *
 * <p>配置 MyBatis-Plus 的分页插件和自动填充功能。</p>
 *
 * <p>功能说明：</p>
 * <ul>
 *   <li>分页插件 - 支持 MySQL 数据库的物理分页</li>
 *   <li>自动填充 - INSERT 时自动填充 createTime 和 updateTime，UPDATE 时自动填充 updateTime</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 注册分页插件
     *
     * @return MybatisPlusInterceptor 配置了 MySQL 分页的拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 注册自动填充处理器
     *
     * <p>INSERT 时填充 createTime 和 updateTime，UPDATE 时填充 updateTime</p>
     *
     * @return MetaObjectHandler 自动填充处理器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
