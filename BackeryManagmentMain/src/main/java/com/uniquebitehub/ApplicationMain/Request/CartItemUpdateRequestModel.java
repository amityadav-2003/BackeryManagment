package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;

@Data
public class CartItemUpdateRequestModel {

    private Long cartItemId;
    private int quantity;
}