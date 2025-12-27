package com.uniquebitehub.ApplicationMain.Request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.OrderType;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private Long userId;
    private Long addressId;          // delivery address
    private OrderType orderType;     // NORMAL / CUSTOM
    private LocalDate deliveryDate;
    private BigDecimal totalAmount;

    private List<CreateOrderItemRequest> items;
}
