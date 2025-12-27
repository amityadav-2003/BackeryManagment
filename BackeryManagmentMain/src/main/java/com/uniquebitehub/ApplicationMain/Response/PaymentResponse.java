package com.uniquebitehub.ApplicationMain.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uniquebitehub.ApplicationMain.Enum.PaymentMethod;
import com.uniquebitehub.ApplicationMain.Enum.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {

//    private Long paymentId;
//    private Long orderId;
//
//    private PaymentStatus paymentStatus;
//    private BigDecimal amount;
//    private String transactionId;
	
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime paidAt;

}
