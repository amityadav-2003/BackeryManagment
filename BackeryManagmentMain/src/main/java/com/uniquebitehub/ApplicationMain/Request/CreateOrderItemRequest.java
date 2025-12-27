package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateOrderItemRequest {

    private Long productId;
    private Long variantId;   // nullable
    private int quantity;
    private BigDecimal price;
}
