package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.product.entity.Banner;
import com.mall.product.mapper.BannerMapper;
import com.mall.product.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图服务实现类
 *
 * <p>实现轮播图的增删改查功能，使用静态变量缓存轮播图列表。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;

    /** 轮播图缓存 */
    private static List<Banner> bannerCache;

    /**
     * 获取轮播图列表（带缓存）
     *
     * @return 轮播图列表
     */
    @Override
    public List<Banner> list() {
        if (bannerCache != null) {
            return bannerCache;
        }
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Banner::getSort);
        bannerCache = bannerMapper.selectList(wrapper);
        return bannerCache;
    }

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     */
    @Override
    public void save(Banner banner) {
        bannerMapper.insert(banner);
        bannerCache = null;
    }

    /**
     * 修改轮播图
     *
     * @param banner 轮播图信息
     */
    @Override
    public void update(Banner banner) {
        bannerMapper.updateById(banner);
        bannerCache = null;
    }

    /**
     * 删除轮播图
     *
     * @param id 轮播图 ID
     */
    @Override
    public void delete(Long id) {
        bannerMapper.deleteById(id);
        bannerCache = null;
    }
}
