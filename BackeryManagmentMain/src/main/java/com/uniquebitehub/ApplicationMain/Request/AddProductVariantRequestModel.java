package com.uniquebitehub.ApplicationMain.Request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AddProductVariantRequestModel {

    private String weight;     // e.g. 500gm, 1kg
    private BigDecimal price;
}
