package com.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Update("UPDATE product SET stock = stock - #{count}, sales = sales + #{count} WHERE id = #{id} AND stock >= #{count}")
    int deductStock(@Param("id") Long id, @Param("count") int count);

    @Update("UPDATE product SET stock = stock + #{count}, sales = sales - #{count} WHERE id = #{id}")
    int restoreStock(@Param("id") Long id, @Param("count") int count);
}
