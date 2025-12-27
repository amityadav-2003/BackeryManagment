package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponse {

    private Long orderItemId;
    private Long productId;
    private String productName;

    private String variant;     // weight / size
    private int quantity;
    private BigDecimal price;
    private BigDecimal itemTotal;
}
