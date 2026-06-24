package com.mall.product.service;

import com.mall.common.result.PageResult;
import com.mall.product.dto.ProductDTO;
import com.mall.product.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 *
 * <p>提供商品的完整管理功能，包括查询、增删改、库存管理等。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     *
     * @param page       页码
     * @param size       每页大小
     * @param categoryId 分类 ID（可选）
     * @param keyword    搜索关键字（可选）
     * @return 分页商品列表
     */
    PageResult<ProductDTO> list(Integer page, Integer size, Long categoryId, String keyword);

    /**
     * 获取商品详情
     *
     * @param id 商品 ID
     * @return 商品详情
     */
    ProductDTO getDetail(Long id);

    /**
     * 获取热门商品（按销量排序，取前 8 个）
     *
     * @return 热门商品列表
     */
    List<ProductDTO> getHotProducts();

    /**
     * 获取新品推荐（按创建时间排序，取前 8 个）
     *
     * @return 新品列表
     */
    List<ProductDTO> getNewProducts();

    /**
     * 新增商品
     *
     * @param product 商品信息
     */
    void save(Product product);

    /**
     * 修改商品
     *
     * @param product 商品信息
     */
    void update(Product product);

    /**
     * 删除商品
     *
     * @param id 商品 ID
     */
    void delete(Long id);

    /**
     * 扣减商品库存
     *
     * @param id    商品 ID
     * @param count 扣减数量
     * @return 是否成功
     */
    boolean deductStock(Long id, int count);

    /**
     * 恢复商品库存
     *
     * @param id    商品 ID
     * @param count 恢复数量
     * @return 是否成功
     */
    boolean restoreStock(Long id, int count);

    /**
     * 统计商品总数
     *
     * @return 商品数量
     */
    long count();
}
