package com.mall.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusText;
    private String addressSnapshot;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private List<OrderItemDTO> items;

    public void setStatusText() {
        String[] texts = {"待付款", "已付款", "已发货", "已完成", "已取消", "已退货"};
        this.statusText = (status != null && status >= 0 && status < texts.length) ? texts[status] : "未知";
    }
}
