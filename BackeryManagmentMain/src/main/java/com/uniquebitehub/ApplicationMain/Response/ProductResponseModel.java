package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.ProductStatus;

import lombok.Data;

@Data
public class ProductResponseModel {
	
	 private Long id;

	    private Long categoryId;
	    private String categoryName;

	    private String name;
	    private String description;

	    private BigDecimal basePrice;
	    private Boolean customizable;

	    private ProductStatus status;

	    private LocalDateTime createdAt;

	    // ========= VARIANTS =========
	    private List<ProductVariantResponseModel> variants;

	    // ========= IMAGES =========
	    private List<String> imageUrls;

}
