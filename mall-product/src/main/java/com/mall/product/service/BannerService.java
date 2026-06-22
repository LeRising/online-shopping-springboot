package com.mall.product.service;

import com.mall.product.entity.Banner;

import java.util.List;

public interface BannerService {

    /**
     * 获取轮播图列表
     */
    List<Banner> list();

    /**
     * 新增轮播图
     */
    void save(Banner banner);

    /**
     * 修改轮播图
     */
    void update(Banner banner);

    /**
     * 删除轮播图
     */
    void delete(Long id);
}
