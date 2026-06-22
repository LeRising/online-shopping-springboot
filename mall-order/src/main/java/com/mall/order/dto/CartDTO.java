package com.mall.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer stock;
    private Integer quantity;
    private Integer checked;
}
