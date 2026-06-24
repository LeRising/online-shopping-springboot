package com.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.order.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车 Mapper 接口
 *
 * <p>提供购物车表的 CRUD 操作，继承 MyBatis-Plus 的 BaseMapper。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
