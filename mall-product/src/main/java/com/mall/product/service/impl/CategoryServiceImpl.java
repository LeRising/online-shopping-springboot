package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.product.entity.Category;
import com.mall.product.mapper.CategoryMapper;
import com.mall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "category:list";
    private static final long TTL_HOURS = 1;

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> list() {
        try {
            Object cached = redisTemplate.opsForValue().get(CACHE_KEY);
            if (cached instanceof List) {
                return (List<Category>) cached;
            }
        } catch (Exception e) {
            // 缓存读取失败，降级查库
        }

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, 0).orderByAsc(Category::getSort);
        List<Category> categories = categoryMapper.selectList(wrapper);

        try {
            redisTemplate.opsForValue().set(CACHE_KEY, categories, TTL_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            // 缓存写入失败不影响业务
        }
        return categories;
    }

    @Override
    public void save(Category category) {
        categoryMapper.insert(category);
        clearCache();
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
        clearCache();
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
        clearCache();
    }

    private void clearCache() {
        try {
            redisTemplate.delete(CACHE_KEY);
        } catch (Exception ignored) {}
    }
}
