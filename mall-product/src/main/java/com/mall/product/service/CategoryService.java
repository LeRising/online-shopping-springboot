package com.mall.product.service;

import com.mall.product.entity.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 获取分类列表
     */
    List<Category> list();

    /**
     * 新增分类
     */
    void save(Category category);

    /**
     * 修改分类
     */
    void update(Category category);

    /**
     * 删除分类
     */
    void delete(Long id);
}
