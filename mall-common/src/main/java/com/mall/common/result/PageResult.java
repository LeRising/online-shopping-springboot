package com.mall.common.result;

import lombok.Data;
import java.util.List;

/**
 * 分页响应封装
 * @param <T>
 */
@Data
public class PageResult<T> {

    // 当前页数据列表
    private List<T> records;
    // 总记录数
    private long total;
    // 当前页码
    private long page;
    // 每页大小
    private long size;

    public PageResult(List<T> records, long total, long page, long size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
