package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;

@Data
public class AddOrderItemRequest {

    private Long productId;
    private Long variantId;
    private Integer quantity;
}
