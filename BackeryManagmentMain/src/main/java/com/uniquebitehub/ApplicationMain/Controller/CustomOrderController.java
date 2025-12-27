package com.uniquebitehub.ApplicationMain.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniquebitehub.ApplicationMain.Request.CustomOrderRequest;
import com.uniquebitehub.ApplicationMain.Request.UpdateCustomOrderStatusRequest;
import com.uniquebitehub.ApplicationMain.Response.CustomOrderResponse;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.CustomOrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/custom-orders")
// @CrossOrigin(origins = "*")
public class CustomOrderController {

	

	@Autowired
	private CustomOrderService customOrderService;

	// ================= CREATE CUSTOM ORDER =================
	@Operation(summary = "Place a custom cake order")
	@PostMapping("/create")
	public RestResponse createCustomOrder(@RequestBody CustomOrderRequest request) {

		log.info("Create custom order | userId: {}", request.getUserId());
		try {
			CustomOrderResponse response = customOrderService.createOrder(request);

			return RestResponse.build().withSuccess("Custom order placed successfully", response);
		} catch (Exception e) {
			log.error("Create custom order failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// ================= GET ORDER BY ID =================
	@Operation(summary = "Get custom order by orderId")
	@GetMapping("/get")
	public RestResponse getOrderById(@RequestParam Long orderId) {

		log.info("Get custom order | orderId: {}", orderId);
		try {
			return RestResponse.build().withSuccess("Custom order fetched successfully",
					customOrderService.getOrderById(orderId));
		} catch (Exception e) {
			log.error("Get custom order failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// ================= GET USER ORDERS =================
	@Operation(summary = "Get all custom orders of a user")
	@GetMapping("/user")
	public RestResponse getOrdersByUser(@RequestParam Long userId) {

		log.info("Get user custom orders | userId: {}", userId);
		try {
			return RestResponse.build().withSuccess("User custom orders fetched successfully",
					customOrderService.getOrdersByUser(userId));
		} catch (Exception e) {
			log.error("Get user orders failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// ================= GET ALL ORDERS (ADMIN) =================
	@Operation(summary = "Get all custom orders (Admin)")
	@GetMapping("/all")
	public RestResponse getAllOrders() {

		log.info("Get all custom orders");
		try {
			return RestResponse.build().withSuccess("All custom orders fetched successfully",
					customOrderService.getAllOrders());
		} catch (Exception e) {
			log.error("Get all orders failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// ================= UPDATE ORDER STATUS =================
	@Operation(summary = "Update custom order status")
	@PutMapping("/updateStatus")
	public RestResponse updateOrderStatus(@RequestBody UpdateCustomOrderStatusRequest request) {

		log.info("Update order status | orderId: {}, status: {}", request.getOrderId(), request.getStatus());
		try {
			CustomOrderResponse response = customOrderService.updateOrderStatus(request);

			return RestResponse.build().withSuccess("Order status updated successfully", response);
		} catch (Exception e) {
			log.error("Update order status failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// ================= CANCEL ORDER =================
	@Operation(summary = "Cancel custom order")
	@PutMapping("/cancel")
	public RestResponse cancelOrder(@RequestParam Long orderId) {

		log.info("Cancel custom order | orderId: {}", orderId);
		try {
			customOrderService.cancelOrder(orderId);
			return RestResponse.build().withSuccess("Custom order cancelled successfully");
		} catch (Exception e) {
			log.error("Cancel order failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// ================= DELETE ORDER (OPTIONAL ADMIN) =================
	@Operation(summary = "Delete custom order (Admin)")
	@DeleteMapping("/delete")
	public RestResponse deleteOrder(@RequestParam Long orderId) {

		log.info("Delete custom order | orderId: {}", orderId);
		try {
			customOrderService.deleteOrder(orderId);
			return RestResponse.build().withSuccess("Custom order deleted successfully");
		} catch (Exception e) {
			log.error("Delete order failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}
}
