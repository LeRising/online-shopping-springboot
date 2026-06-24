package com.mall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.order.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单主表 Mapper 接口
 *
 * <p>提供订单主表的 CRUD 操作，继承 MyBatis-Plus 的 BaseMapper。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}
