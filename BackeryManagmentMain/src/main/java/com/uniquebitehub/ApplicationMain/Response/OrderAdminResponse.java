package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uniquebitehub.ApplicationMain.Enum.*;

import lombok.Data;

@Data
public class OrderAdminResponse {

    private Long orderId;
    private Long userId;

    private OrderType orderType;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
