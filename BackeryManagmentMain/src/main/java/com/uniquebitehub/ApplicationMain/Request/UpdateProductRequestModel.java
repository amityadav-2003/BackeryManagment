package com.uniquebitehub.ApplicationMain.Request;

import java.io.ObjectInputFilter.Status;
import java.math.BigDecimal;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.ProductStatus;

import lombok.Data;

@Data
public class UpdateProductRequestModel {
	
	  // REQUIRED
    private Long id;

    // BASIC PRODUCT DETAILS
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Boolean customizable;

    // OPTIONAL (future use)
    private ProductStatus status;          // ACTIVE / INACTIVE
    private Long categoryId;

    // VARIANTS UPDATE / ADD
    private List<UpdateProductVariantRequestModel> variants;

}
