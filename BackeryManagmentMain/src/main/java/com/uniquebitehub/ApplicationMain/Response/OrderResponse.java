package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderType;

import lombok.Data;

@Data
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private OrderType orderType;
    private OrderStatus orderStatus;

    private BigDecimal totalAmount;
    private LocalDate deliveryDate;
    private LocalDateTime createdAt;

    private AddressResponseModel deliveryAddress;
    
    private List<OrderItemResponse> items;

	
}
