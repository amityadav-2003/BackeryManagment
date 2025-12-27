package com.uniquebitehub.ApplicationMain.Request;

import com.uniquebitehub.ApplicationMain.Enum.CustomOrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;

import lombok.Data;

@Data
public class UpdateCustomOrderStatusRequest {
	
	  private Long orderId;
	    private CustomOrderStatus status;

}
