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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 *
 * <p>实现商品的完整管理功能，包括查询、增删改、库存管理等。</p>
 *
 * <p>核心业务逻辑：</p>
 * <ul>
 *   <li>分页查询支持分类筛选和关键字搜索</li>
 *   <li>热门商品和新品推荐使用静态缓存</li>
 *   <li>库存扣减使用乐观锁（SQL 条件判断）</li>
 * </ul>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    /** 热门商品缓存 */
    private static List<ProductDTO> hotProductsCache;
    /** 新品缓存 */
    private static List<ProductDTO> newProductsCache;

    /**
     * 分页查询商品列表
     *
     * <p>支持按分类筛选和关键字搜索，按销量降序排序。</p>
     *
     * @param page       页码
     * @param size       每页大小
     * @param categoryId 分类 ID（可选）
     * @param keyword    搜索关键字（可选）
     * @return 分页商品列表
     */
    @Override
    public PageResult<ProductDTO> list(Integer page, Integer size, Long categoryId, String keyword) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        if (categoryId != null) {
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

    /**
     * 获取商品详情
     *
     * @param id 商品 ID
     * @return 商品详情
     */
    @Override
    public ProductDTO getDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        return toDTO(product);
    }

    /**
     * 获取热门商品（带缓存）
     *
     * <p>按销量排序，取前 8 个商品。</p>
     *
     * @return 热门商品列表
     */
    @Override
    public List<ProductDTO> getHotProducts() {
        if (hotProductsCache != null) {
            return hotProductsCache;
        }
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .orderByDesc(Product::getSales)
                .last("LIMIT 8");
        hotProductsCache = productMapper.selectList(wrapper).stream()
                .map(this::toDTO).collect(Collectors.toList());
        return hotProductsCache;
    }

    /**
     * 获取新品推荐（带缓存）
     *
     * <p>按创建时间排序，取前 8 个商品。</p>
     *
     * @return 新品列表
     */
    @Override
    public List<ProductDTO> getNewProducts() {
        if (newProductsCache != null) {
            return newProductsCache;
        }
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .orderByDesc(Product::getCreateTime)
                .last("LIMIT 8");
        newProductsCache = productMapper.selectList(wrapper).stream()
                .map(this::toDTO).collect(Collectors.toList());
        return newProductsCache;
    }

    /**
     * 新增商品
     *
     * @param product 商品信息
     */
    @Override
    public void save(Product product) {
        product.setSales(0);
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        productMapper.insert(product);
        clearCache();
    }

    /**
     * 修改商品
     *
     * @param product 商品信息
     */
    @Override
    public void update(Product product) {
        productMapper.updateById(product);
        clearCache();
    }

    /**
     * 删除商品
     *
     * @param id 商品 ID
     */
    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
        clearCache();
    }

    /**
     * 扣减商品库存
     *
     * <p>使用乐观锁，通过 SQL 条件判断库存是否充足。</p>
     *
     * @param id    商品 ID
     * @param count 扣减数量
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deductStock(Long id, int count) {
        int rows = productMapper.deductStock(id, count);
        if (rows > 0) {
            clearCache();
            return true;
        }
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在: " + id);
        }
        throw new BusinessException("商品库存不足: " + product.getName());
    }

    /**
     * 恢复商品库存
     *
     * @param id    商品 ID
     * @param count 恢复数量
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean restoreStock(Long id, int count) {
        int rows = productMapper.restoreStock(id, count);
        clearCache();
        return rows > 0;
    }

    /**
     * 统计商品总数
     *
     * @return 商品数量
     */
    @Override
    public long count() {
        return productMapper.selectCount(null);
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        hotProductsCache = null;
        newProductsCache = null;
    }

    /**
     * 将商品实体转换为 DTO
     *
     * @param product 商品实体
     * @return 商品 DTO
     */
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
