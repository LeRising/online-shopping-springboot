package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.product.entity.Category;
import com.mall.product.mapper.CategoryMapper;
import com.mall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类服务实现类
 *
 * <p>实现商品分类的增删改查功能，使用静态变量缓存分类列表。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    /** 分类列表缓存 */
    private static List<Category> categoryCache;

    /**
     * 获取顶级分类列表（带缓存）
     *
     * @return 分类列表
     */
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

    /**
     * 新增分类
     *
     * @param category 分类信息
     */
    @Override
    public void save(Category category) {
        categoryMapper.insert(category);
        categoryCache = null;
    }

    /**
     * 修改分类
     *
     * @param category 分类信息
     */
    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
        categoryCache = null;
    }

    /**
     * 删除分类
     *
     * @param id 分类 ID
     */
    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
        categoryCache = null;
    }
}
