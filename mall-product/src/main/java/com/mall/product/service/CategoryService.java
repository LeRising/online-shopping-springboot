package com.mall.product.service;

import com.mall.product.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 *
 * <p>提供商品分类的增删改查功能。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface CategoryService {

    /**
     * 获取顶级分类列表（按排序值升序）
     *
     * @return 分类列表
     */
    List<Category> list();

    /**
     * 新增分类
     *
     * @param category 分类信息
     */
    void save(Category category);

    /**
     * 修改分类
     *
     * @param category 分类信息
     */
    void update(Category category);

    /**
     * 删除分类
     *
     * @param id 分类 ID
     */
    void delete(Long id);
}
