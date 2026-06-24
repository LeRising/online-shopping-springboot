package com.mall.product.service;

import com.mall.product.entity.Banner;

import java.util.List;

/**
 * 轮播图服务接口
 *
 * <p>提供轮播图的增删改查功能。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
public interface BannerService {

    /**
     * 获取轮播图列表（按排序值升序）
     *
     * @return 轮播图列表
     */
    List<Banner> list();

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     */
    void save(Banner banner);

    /**
     * 修改轮播图
     *
     * @param banner 轮播图信息
     */
    void update(Banner banner);

    /**
     * 删除轮播图
     *
     * @param id 轮播图 ID
     */
    void delete(Long id);
}
