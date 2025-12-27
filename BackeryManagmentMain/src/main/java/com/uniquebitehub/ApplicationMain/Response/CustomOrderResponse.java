package com.uniquebitehub.ApplicationMain.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uniquebitehub.ApplicationMain.Enum.CustomOrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomOrderResponse {
    private Long OrderId;
    private Long userId;
    private String occasion;
    private String weight;
    private String flavor;
    private String messageOnCake;
    private String designReference;
    private LocalDate deliveryDate;
    private CustomOrderStatus status;
    private LocalDateTime createdAt;
}