package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderInvoiceResponse {

    private Long orderId;
    private String customerName;
    private String address;

    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String paymentMethod;
}
