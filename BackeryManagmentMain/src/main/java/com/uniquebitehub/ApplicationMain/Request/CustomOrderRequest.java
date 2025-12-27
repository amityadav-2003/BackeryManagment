package com.uniquebitehub.ApplicationMain.Request;

import java.time.LocalDate;

import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;

import lombok.Data;

@Data
public class CustomOrderRequest {
	 private Long userId;
	    private String occasion;
	    private String weight;
	    private String flavor;
	    private String messageOnCake;
	    private String designReference;
	    private LocalDate deliveryDate;
	    private OrderStatus status;
	}