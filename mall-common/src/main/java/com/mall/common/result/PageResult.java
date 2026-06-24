package com.mall.common.result;

import lombok.Data;
import java.util.List;

/**
 * 分页响应封装
 *
 * <p>用于返回分页查询结果，包含当前页数据、总记录数、页码和每页大小。</p>
 *
 * @param <T> 列表元素类型
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class PageResult<T> {

    /** 当前页数据列表 */
    private List<T> records;

    /** 总记录数 */
    private long total;

    /** 当前页码（从 1 开始） */
    private long page;

    /** 每页大小 */
    private long size;

    /**
     * 构造分页结果
     *
     * @param records 当前页数据列表
     * @param total   总记录数
     * @param page    当前页码
     * @param size    每页大小
     */
    public PageResult(List<T> records, long total, long page, long size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
