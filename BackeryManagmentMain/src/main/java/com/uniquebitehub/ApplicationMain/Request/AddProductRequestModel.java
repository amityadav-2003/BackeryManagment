package com.uniquebitehub.ApplicationMain.Request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class AddProductRequestModel {
	
	 private Long categoryId;
	    private String name;
	    private String description;
	    private BigDecimal basePrice;
	    private Boolean customizable;

	    // ========= VARIANTS =========
	    private List<AddProductVariantRequestModel> variants;
	  
}
