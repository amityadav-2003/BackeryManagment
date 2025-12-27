package com.uniquebitehub.ApplicationMain.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderType;
import com.uniquebitehub.ApplicationMain.Enum.PaymentStatus;
import com.uniquebitehub.ApplicationMain.Request.AddOrderItemRequest;
import com.uniquebitehub.ApplicationMain.Request.CreateOrderRequest;
import com.uniquebitehub.ApplicationMain.Request.PaymentRequest;
import com.uniquebitehub.ApplicationMain.Response.OrderAdminResponse;
import com.uniquebitehub.ApplicationMain.Response.OrderInvoiceResponse;
import com.uniquebitehub.ApplicationMain.Response.OrderResponse;
import com.uniquebitehub.ApplicationMain.Response.OrderTrackingResponse;
import com.uniquebitehub.ApplicationMain.Response.PaymentResponse;
import com.uniquebitehub.ApplicationMain.Response.PaymentStatusResponse;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.OrderService;
import com.uniquebitehub.ApplicationMain.Service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/order")
@Tag(name = "Order Management", description = "APIs for order management")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ObjectMapper objectMapper;

	// ================= CREATE ORDER =================
	@Operation(summary = "Create New Order")
	@PostMapping("/create")
	public RestResponse createOrder(
			@Parameter(description = "Order data in JSON format") @RequestBody String orderJson) {

		try {
			log.info("Creating new order");

			// Convert JSON to Request Model
			CreateOrderRequest request = objectMapper.readValue(orderJson, CreateOrderRequest.class);

			// Validate required fields
			if (request.getItems() == null || request.getItems().isEmpty()) {
				return RestResponse.build().withError("Order items cannot be empty").withStatusCode("VALIDATION_ERROR");
			}

			if (request.getAddressId() == null) {
				return RestResponse.build().withError("Address ID is required").withStatusCode("VALIDATION_ERROR");
			}

			if (request.getUserId() == null) {
				return RestResponse.build().withError("User ID is required").withStatusCode("VALIDATION_ERROR");
			}

			// Create order
			OrderResponse response = orderService.createOrder(request);

			return RestResponse.build().withSuccess("Order created successfully").withData(response)
					.withStatusCode("ORDER_CREATED");

		} catch (Exception e) {
			log.error("Create order failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("ORDER_CREATION_FAILED");
		}
	}

	// ================= GET ORDER BY ID =================
	@Operation(summary = "Get Order By ID")
	@GetMapping("/findById")
	public RestResponse getOrderById(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "User ID") @RequestParam(required = false) Long userId) {

		try {
			log.info("Getting order by ID: {}", orderId);

			OrderResponse response = orderService.getOrderById(orderId, userId);

			return RestResponse.build().withSuccess("Order found").withData(response).withStatusCode("ORDER_FOUND");

		} catch (Exception e) {
			log.error("Get order by ID failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("ORDER_NOT_FOUND");
		}
	}

	// ================= GET USER ORDERS =================
	@Operation(summary = "Get All Orders for User")
	@GetMapping("/user")
	public RestResponse getUserOrders(@Parameter(description = "User ID") @RequestParam Long userId,
			@Parameter(description = "Order status filter") @RequestParam(required = false) OrderStatus status,
			@Parameter(description = "Start date filter") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@Parameter(description = "End date filter") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		try {
			log.info("Getting user orders for user ID: {}", userId);

			List<OrderResponse> orders = orderService.getUserOrders(userId, status, startDate, endDate);

			return RestResponse.build().withSuccess("User orders retrieved").withData(orders)
					.withStatusCode("ORDERS_FOUND");

		} catch (Exception e) {
			log.error("Get user orders failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("RETRIEVE_FAILED");
		}
	}

	// ================= CANCEL ORDER =================
	@Operation(summary = "Cancel Order")
	@PutMapping("/cancel")
	public RestResponse cancelOrder(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "User ID") @RequestParam Long userId,
			@Parameter(description = "Cancellation reason") @RequestParam(required = false) String reason) {

		try {
			log.info("Cancelling order ID: {} by user ID: {}", orderId, userId);

			OrderResponse response = orderService.cancelOrder(orderId, reason);

			return RestResponse.build().withSuccess("Order cancelled successfully").withData(response)
					.withStatusCode("ORDER_CANCELLED");

		} catch (Exception e) {
			log.error("Cancel order failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("CANCELLATION_FAILED");
		}
	}

	// ================= TRACK ORDER =================
	@Operation(summary = "Track Order Status")
	@GetMapping("/track")
	public RestResponse trackOrder(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "User ID") @RequestParam(required = false) Long userId) {

		try {
			log.info("Tracking order ID: {}", orderId);

			OrderTrackingResponse response = orderService.trackOrder(orderId, userId);

			return RestResponse.build().withSuccess("Order tracking retrieved").withData(response)
					.withStatusCode("TRACKING_FOUND");

		} catch (Exception e) {
			log.error("Track order failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("TRACKING_FAILED");
		}
	}

	// ================= PROCESS PAYMENT =================
	@Operation(summary = "Process Payment for Order")
	@PostMapping("/payment/process")
	public RestResponse processPayment(@RequestBody PaymentRequest request) {

		try {
			log.info("Processing payment for OrderId: {}", request.getOrderId());

			// ================= VALIDATION =================
			if (request.getOrderId() == null) {
				return RestResponse.build().withError("Order ID is required").withStatusCode("VALIDATION_ERROR");
			}

			if (request.getPaymentMethod() == null) {
				return RestResponse.build().withError("Payment method is required").withStatusCode("VALIDATION_ERROR");
			}

//	        if (request.getUserId() == null) {
//	            return RestResponse.build()
//	                    .withError("User ID is required")
//	                    .withStatusCode("VALIDATION_ERROR");
//	        }

			// ================= PROCESS PAYMENT =================
			PaymentResponse response = paymentService.processPayment(request);

			return RestResponse.build().withSuccess("Payment processed successfully", response)
					.withStatusCode("PAYMENT_PROCESSED");

		} catch (Exception e) {
			log.error("Payment processing failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("PAYMENT_FAILED");
		}
	}

	// ================= GET PAYMENT STATUS =================
	@Operation(summary = "Get Payment Status")
	@GetMapping("/payment/status")
	public RestResponse getPaymentStatus(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "User ID") @RequestParam(required = false) Long userId) {

		try {
			log.info("Getting payment status for order ID: {}", orderId);

			PaymentResponse response = paymentService.getPaymentStatus(orderId);

			return RestResponse.build().withSuccess("Payment status retrieved").withData(response)
					.withStatusCode("PAYMENT_STATUS_FOUND");

		} catch (Exception e) {
			log.error("Get payment status failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("PAYMENT_STATUS_NOT_FOUND");
		}
	}

	// ================= ADMIN: GET ALL ORDERS =================
	@Operation(summary = "Admin - Get All Orders")
	@GetMapping("/admin/all")
	public RestResponse getAllOrders(
			@Parameter(description = "Order status filter") @RequestParam(required = false) OrderStatus orderStatus,
			@Parameter(description = "Payment status filter") @RequestParam(required = false) PaymentStatus paymentStatus,
			@Parameter(description = "Order type filter") @RequestParam(required = false) OrderType orderType,
			@Parameter(description = "From date filter") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@Parameter(description = "To date filter") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

		try {
			log.info("Admin: Getting all orders");

			List<OrderAdminResponse> orders = orderService.getAllOrders(orderStatus, paymentStatus, orderType, fromDate,
					toDate);

			return RestResponse.build().withSuccess("All orders retrieved").withData(orders)
					.withStatusCode("ORDERS_FOUND");

		} catch (Exception e) {
			log.error("Admin get all orders failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("RETRIEVE_FAILED");
		}
	}

	// ================= ADMIN: UPDATE ORDER STATUS =================
	@Operation(summary = "Admin - Update Order Status")
	@PutMapping("/admin/updateStatus")
	public RestResponse updateOrderStatus(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "New order status") @RequestParam OrderStatus status,
			@Parameter(description = "Status update notes") @RequestParam(required = false) String notes) {

		try {
			log.info("Admin: Updating order status for ID: {} to {}", orderId, status);

			OrderResponse response = orderService.updateOrderStatus(orderId, status);

			return RestResponse.build().withSuccess("Order status updated").withData(response)
					.withStatusCode("STATUS_UPDATED");

		} catch (Exception e) {
			log.error("Admin update order status failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("STATUS_UPDATE_FAILED");
		}
	}

	// ================= ADMIN: UPDATE DELIVERY DATE =================
	@Operation(summary = "Admin - Update Delivery Date")
	@PutMapping("/admin/updateDeliveryDate")
	public RestResponse updateDeliveryDate(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "New delivery date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate) {

		try {
			log.info("Admin: Updating delivery date for order ID: {} to {}", orderId, deliveryDate);

			OrderResponse response = orderService.updateDeliveryDate(orderId, deliveryDate);

			return RestResponse.build().withSuccess("Delivery date updated").withData(response)
					.withStatusCode("DELIVERY_DATE_UPDATED");

		} catch (Exception e) {
			log.error("Admin update delivery date failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("DELIVERY_DATE_UPDATE_FAILED");
		}
	}

	// ================= ADMIN: GET ORDER STATISTICS =================
//	@Operation(summary = "Admin - Get Order Statistics")
//	@GetMapping("/admin/statistics")
//	public RestResponse getOrderStatistics(
//			@Parameter(description = "Start date for statistics") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//			@Parameter(description = "End date for statistics") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//		try {
//			log.info("Admin: Getting order statistics");
//
//			OrderStatisticsResponse statistics = orderService.getOrderStatistics(startDate, endDate);
//
//			return RestResponse.build().withSuccess("Order statistics retrieved").withData(statistics)
//					.withStatusCode("STATISTICS_FOUND");
//
//		} catch (Exception e) {
//			log.error("Admin get statistics failed", e);
//			return RestResponse.build().withError(e.getMessage()).withStatusCode("STATISTICS_FAILED");
//		}
//	}

	// ================= ADD ITEM TO ORDER =================
	@Operation(summary = "Add Item to Existing Order")
	@PostMapping("/add-item")
	public RestResponse addOrderItem(@Parameter(description = "Order ID", required = true) @RequestParam Long orderId,

			@Parameter(description = "User ID", required = true) @RequestParam Long userId,

			@RequestBody AddOrderItemRequest request) {
		try {
			log.info("Adding item to orderId={} by userId={}", orderId, userId);

			// Validation
			if (request.getProductId() == null) {
				return RestResponse.build().withError("Product ID is required").withStatusCode("VALIDATION_ERROR");
			}

			if (request.getQuantity() == null || request.getQuantity() <= 0) {
				return RestResponse.build().withError("Quantity must be greater than zero")
						.withStatusCode("VALIDATION_ERROR");
			}

			OrderResponse response = orderService.addOrderItem(orderId, userId, request);

			return RestResponse.build().withSuccess("Item added to order successfully").withData(response)
					.withStatusCode("ITEM_ADDED");

		} catch (Exception e) {
			log.error("Failed to add item to orderId={}", orderId, e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("ITEM_ADD_FAILED");
		}
	}

	// ================= REMOVE ITEM FROM ORDER =================
	@Operation(summary = "Remove Item from Order")
	@DeleteMapping("/removeItem")
	public RestResponse removeOrderItem(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "User ID") @RequestParam Long userId,
			@Parameter(description = "Order Item ID") @RequestParam Long itemId) {

		try {
			log.info("Removing item ID: {} from order ID: {} by user ID: {}", itemId, orderId, userId);

			OrderResponse response = orderService.removeOrderItem(orderId, userId, itemId);

			return RestResponse.build().withSuccess("Item removed from order").withData(response)
					.withStatusCode("ITEM_REMOVED");

		} catch (Exception e) {
			log.error("Remove item from order failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("ITEM_REMOVE_FAILED");
		}
	}

	// ================= UPDATE ITEM QUANTITY =================
	@Operation(summary = "Update Item Quantity in Order")
	@PutMapping("/updateItemQuantity")
	public RestResponse updateItemQuantity(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "Order Item ID") @RequestParam Long itemId,
			@Parameter(description = "New quantity") @RequestParam Integer quantity) {

		try {
			log.info("Updating quantity for item ID: {} in order ID: {} to {}", itemId, orderId, quantity);

			if (quantity <= 0) {
				return RestResponse.build().withError("Quantity must be greater than 0")
						.withStatusCode("VALIDATION_ERROR");
			}

			OrderResponse response = orderService.updateItemQuantity(orderId, itemId, quantity);

			return RestResponse.build().withSuccess("Item quantity updated").withData(response)
					.withStatusCode("QUANTITY_UPDATED");

		} catch (Exception e) {
			log.error("Update item quantity failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("QUANTITY_UPDATE_FAILED");
		}
	}

	// ================= GET ORDER INVOICE =================
	@Operation(summary = "Get Order Invoice")
	@GetMapping("/invoice")
	public RestResponse getOrderInvoice(@Parameter(description = "Order ID") @RequestParam Long orderId,
			@Parameter(description = "User ID") @RequestParam(required = false) Long userId) {

		try {
			log.info("Getting invoice for order ID: {}", orderId);

			OrderResponse invoice = orderService.getOrderById(orderId, userId);

			return RestResponse.build().withSuccess("Invoice retrieved").withData(invoice)
					.withStatusCode("INVOICE_FOUND");

		} catch (Exception e) {
			log.error("Get invoice failed", e);
			return RestResponse.build().withError(e.getMessage()).withStatusCode("INVOICE_NOT_FOUND");
		}
	}
}