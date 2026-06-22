package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.product.entity.Banner;
import com.mall.product.mapper.BannerMapper;
import com.mall.product.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;

    /** 轮播图缓存 */
    private static List<Banner> bannerCache;

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

    @Override
    public void save(Banner banner) {
        bannerMapper.insert(banner);
        bannerCache = null;
    }

    @Override
    public void update(Banner banner) {
        bannerMapper.updateById(banner);
        bannerCache = null;
    }

    @Override
    public void delete(Long id) {
        bannerMapper.deleteById(id);
        bannerCache = null;
    }
}
