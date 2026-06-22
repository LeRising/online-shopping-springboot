package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.product.entity.Category;
import com.mall.product.mapper.CategoryMapper;
import com.mall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    /** 分类列表缓存 */
    private static List<Category> categoryCache;

    @Override
    public List<Category> list() {
        if (categoryCache != null) {
            return categoryCache;
        }
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, 0).orderByAsc(Category::getSort);
        categoryCache = categoryMapper.selectList(wrapper);
        return categoryCache;
    }

    @Override
    public void save(Category category) {
        categoryMapper.insert(category);
        categoryCache = null;
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
        categoryCache = null;
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
        categoryCache = null;
    }
}
