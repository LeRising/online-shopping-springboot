package com.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 *
 * <p>提供用户表的 CRUD 操作，继承 MyBatis-Plus 的 BaseMapper。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
