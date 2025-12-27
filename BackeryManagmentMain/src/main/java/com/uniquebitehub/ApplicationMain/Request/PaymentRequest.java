package com.uniquebitehub.ApplicationMain.Request;

import java.math.BigDecimal;

import com.uniquebitehub.ApplicationMain.Enum.PaymentMethod;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long orderId;
    private PaymentMethod paymentMethod; // UPI / CASH / CARD
    private String transactionId;
    private BigDecimal amount;
}
