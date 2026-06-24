package com.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项 Mapper 接口
 *
 * <p>提供订单项表的 CRUD 操作，继承 MyBatis-Plus 的 BaseMapper。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
