package com.uniquebitehub.ApplicationMain.Converter.Entity;



import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Response.OrderItemResponse;
import com.uniquebitehub.ApplicationMain.Response.OrderResponse;

@Component
public class OrderEntityToModelConverter {

	
	  public OrderResponse convert(Order order) {

	        OrderResponse response = new OrderResponse();

	        response.setOrderId(order.getId());
	        response.setUserId(order.getUser().getId());
	        response.setOrderType(order.getOrderType());
	        response.setOrderStatus(order.getOrderStatus());
	      
	        response.setTotalAmount(order.getTotalAmount());
	        response.setDeliveryDate(order.getDeliveryDate());
	        response.setCreatedAt(order.getCreatedAt());

	        if (order.getOrderItems() != null) {
	            response.setItems(
	                order.getOrderItems()
	                     .stream()
	                     .map(this::convertItem)
	                     .collect(Collectors.toList())
	            );
	        }

	        return response;
	    }

	    private OrderItemResponse convertItem(com.uniquebitehub.ApplicationMain.Entity.OrderItem item) {

	        OrderItemResponse res = new OrderItemResponse();

	        res.setOrderItemId(item.getId());
	        res.setProductId(item.getProduct().getId());
	        res.setProductName(item.getProduct().getName());

	        if (item.getProductVariant() != null) {
	            res.setVariant(item.getProductVariant().getWeight());
	        }

	        res.setPrice(item.getPrice());
	        res.setQuantity(item.getQuantity());
	        res.setItemTotal(
	            item.getPrice().multiply(
	                java.math.BigDecimal.valueOf(item.getQuantity())
	            )
	        );

	        return res;
	    }
}
