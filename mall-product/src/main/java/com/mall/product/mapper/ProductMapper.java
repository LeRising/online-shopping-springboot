package com.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品 Mapper 接口
 *
 * <p>提供商品表的 CRUD 操作和库存管理方法，继承 MyBatis-Plus 的 BaseMapper。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 扣减商品库存（乐观锁）
     *
     * <p>使用 SQL 直接更新，通过 stock >= #{count} 条件保证库存不会扣减为负数。</p>
     *
     * @param id    商品 ID
     * @param count 扣减数量
     * @return 受影响行数（0 表示库存不足）
     */
    @Update("UPDATE product SET stock = stock - #{count}, sales = sales + #{count} WHERE id = #{id} AND stock >= #{count}")
    int deductStock(@Param("id") Long id, @Param("count") int count);

    /**
     * 恢复商品库存
     *
     * @param id    商品 ID
     * @param count 恢复数量
     * @return 受影响行数
     */
    @Update("UPDATE product SET stock = stock + #{count}, sales = sales - #{count} WHERE id = #{id}")
    int restoreStock(@Param("id") Long id, @Param("count") int count);
}
