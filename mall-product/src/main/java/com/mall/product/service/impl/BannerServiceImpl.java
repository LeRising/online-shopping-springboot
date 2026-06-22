package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.product.entity.Banner;
import com.mall.product.mapper.BannerMapper;
import com.mall.product.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "banner:list";
    private static final long TTL_HOURS = 1;

    @Override
    @SuppressWarnings("unchecked")
    public List<Banner> list() {
        try {
            Object cached = redisTemplate.opsForValue().get(CACHE_KEY);
            if (cached instanceof List) {
                return (List<Banner>) cached;
            }
        } catch (Exception e) {
            // 缓存读取失败，降级查库
        }

        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Banner::getSort);
        List<Banner> banners = bannerMapper.selectList(wrapper);

        try {
            redisTemplate.opsForValue().set(CACHE_KEY, banners, TTL_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            // 缓存写入失败不影响业务
        }
        return banners;
    }

    @Override
    public void save(Banner banner) {
        bannerMapper.insert(banner);
        clearCache();
    }

    @Override
    public void update(Banner banner) {
        bannerMapper.updateById(banner);
        clearCache();
    }

    @Override
    public void delete(Long id) {
        bannerMapper.deleteById(id);
        clearCache();
    }

    private void clearCache() {
        try {
            redisTemplate.delete(CACHE_KEY);
        } catch (Exception ignored) {}
    }
}
