package com.uniquebitehub.ApplicationMain.Response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponseModel {

    private Long cartId;
    private List<CartItemResponseModel> items;
    private BigDecimal totalAmount;
    private int totalItems;
}
