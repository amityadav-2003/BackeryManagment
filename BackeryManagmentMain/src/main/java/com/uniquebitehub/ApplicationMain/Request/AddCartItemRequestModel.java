package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;

@Data
public class AddCartItemRequestModel {

    private Long userId;
    private Long productId;
    private Long variantId;   // nullable
    private Integer quantity;
}
