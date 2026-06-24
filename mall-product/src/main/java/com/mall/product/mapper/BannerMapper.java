package com.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.product.entity.Banner;
import org.apache.ibatis.annotations.Mapper;

/**
 * 轮播图 Mapper 接口
 *
 * <p>提供轮播图表的 CRUD 操作，继承 MyBatis-Plus 的 BaseMapper。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Mapper
public interface BannerMapper extends BaseMapper<Banner> {
}
