package com.mall.product.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.exception.BusinessException;
import com.mall.common.result.PageResult;
import com.mall.product.dto.ProductDTO;
import com.mall.product.entity.Category;
import com.mall.product.entity.Product;
import com.mall.product.mapper.CategoryMapper;
import com.mall.product.mapper.ProductMapper;
import com.mall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PRODUCT_DETAIL = "product:detail:";
    private static final long DETAIL_TTL_MINUTES = 30;

    @Override
    public PageResult<ProductDTO> list(Integer page, Integer size, Long categoryId, String keyword) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        if (categoryId != null) {
            // 查询该分类及其子分类
            List<Long> categoryIds = categoryMapper.selectList(
                    new LambdaQueryWrapper<Category>().eq(Category::getParentId, categoryId))
                    .stream().map(Category::getId).collect(Collectors.toList());
            categoryIds.add(categoryId);
            wrapper.in(Product::getCategoryId, categoryIds);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getSales);

        Page<Product> pageResult = productMapper.selectPage(new Page<>(page, size), wrapper);
        List<ProductDTO> records = pageResult.getRecords().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return new PageResult<>(records, pageResult.getTotal(), page, size);
    }

    @Override
    public ProductDTO getDetail(Long id) {
        String cacheKey = CACHE_PRODUCT_DETAIL + id;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached instanceof ProductDTO) {
                return (ProductDTO) cached;
            }
            if (cached != null) {
                redisTemplate.delete(cacheKey);
            }
        } catch (Exception e) {
            try { redisTemplate.delete(cacheKey); } catch (Exception ignored) {}
        }

        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        ProductDTO dto = toDTO(product);

        try {
            redisTemplate.opsForValue().set(cacheKey, dto, DETAIL_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception ignored) {}
        return dto;
    }

    @Override
    public List<ProductDTO> getHotProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .orderByDesc(Product::getSales)
                .last("LIMIT 8");
        return productMapper.selectList(wrapper).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getNewProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .orderByDesc(Product::getCreateTime)
                .last("LIMIT 8");
        return productMapper.selectList(wrapper).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        product.setSales(0);
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        productMapper.insert(product);
    }

    @Override
    public void update(Product product) {
        productMapper.updateById(product);
        redisTemplate.delete(CACHE_PRODUCT_DETAIL + product.getId());
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
        redisTemplate.delete(CACHE_PRODUCT_DETAIL + id);
    }

    @Override
    @Transactional
    public boolean deductStock(Long id, int count) {
        int rows = productMapper.deductStock(id, count);
        if (rows > 0) {
            try { redisTemplate.delete(CACHE_PRODUCT_DETAIL + id); } catch (Exception ignored) {}
            return true;
        }
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在: " + id);
        }
        throw new BusinessException("商品库存不足: " + product.getName());
    }

    @Override
    @Transactional
    public boolean restoreStock(Long id, int count) {
        int rows = productMapper.restoreStock(id, count);
        try { redisTemplate.delete(CACHE_PRODUCT_DETAIL + id); } catch (Exception ignored) {}
        return rows > 0;
    }

    @Override
    public long count() {
        return productMapper.selectCount(null);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setStock(product.getStock());
        dto.setCategoryId(product.getCategoryId());
        dto.setImage(product.getImage());
        dto.setImages(product.getImages());
        dto.setStatus(product.getStatus());
        dto.setSales(product.getSales());

        if (product.getCategoryId() != null) {
            try {
                Category category = categoryMapper.selectById(product.getCategoryId());
                if (category != null) {
                    dto.setCategoryName(category.getName());
                }
            } catch (Exception ignored) {}
        }
        return dto;
    }
}
