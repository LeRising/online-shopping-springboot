package com.mall.product.service;

import com.mall.common.result.PageResult;
import com.mall.product.dto.ProductDTO;
import com.mall.product.entity.Product;

import java.util.List;

public interface ProductService {

    PageResult<ProductDTO> list(Integer page, Integer size, Long categoryId, String keyword);

    ProductDTO getDetail(Long id);

    List<ProductDTO> getHotProducts();

    List<ProductDTO> getNewProducts();

    void save(Product product);

    void update(Product product);

    void delete(Long id);

    boolean deductStock(Long id, int count);

    boolean restoreStock(Long id, int count);

    long count();
}
