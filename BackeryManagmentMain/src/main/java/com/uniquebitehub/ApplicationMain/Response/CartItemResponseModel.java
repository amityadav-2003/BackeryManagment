package com.uniquebitehub.ApplicationMain.Response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponseModel {
	private Long cartItemId;
    private Long productId;
    private String productName;
    private String variant;
    private BigDecimal price;
    private int quantity;
    private BigDecimal itemTotal;
    
    private String imageUrl;
}
