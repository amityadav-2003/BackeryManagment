package com.uniquebitehub.ApplicationMain.Response;

import com.uniquebitehub.ApplicationMain.Enum.PaymentStatus;

import lombok.Data;

@Data
public class PaymentStatusResponse {

    private Long orderId;
    private PaymentStatus paymentStatus;
}