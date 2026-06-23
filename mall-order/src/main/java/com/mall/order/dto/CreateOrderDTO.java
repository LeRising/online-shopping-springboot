package com.mall.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderDTO {
    private Long addressId;
    private List<Long> cartIds;
}
