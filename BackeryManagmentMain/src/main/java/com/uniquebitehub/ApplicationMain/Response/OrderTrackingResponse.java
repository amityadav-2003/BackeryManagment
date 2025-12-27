package com.uniquebitehub.ApplicationMain.Response;

import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;
import lombok.Data;

@Data
public class OrderTrackingResponse {

    private Long orderId;
    private OrderStatus currentStatus;
    private String message;
}
