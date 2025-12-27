package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductVariantResponseModel {

	private Long id;
    private String weight;
    private BigDecimal price;
}
