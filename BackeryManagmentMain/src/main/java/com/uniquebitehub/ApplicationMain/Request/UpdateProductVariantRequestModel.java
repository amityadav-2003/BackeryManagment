package com.uniquebitehub.ApplicationMain.Request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateProductVariantRequestModel {

	
	 private Long id;        // null = new variant
	    private String weight;
	    private BigDecimal price;
}
