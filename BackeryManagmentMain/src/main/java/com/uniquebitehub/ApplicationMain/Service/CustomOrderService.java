package com.uniquebitehub.ApplicationMain.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Entity.CustomOrder;
import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Enum.CustomOrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;
import com.uniquebitehub.ApplicationMain.Repository.CustomOrderRepository;
import com.uniquebitehub.ApplicationMain.Repository.UserRepository;
import com.uniquebitehub.ApplicationMain.Request.CustomOrderRequest;
import com.uniquebitehub.ApplicationMain.Request.UpdateCustomOrderStatusRequest;
import com.uniquebitehub.ApplicationMain.Response.CustomOrderResponse;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Transactional
@Service
@Log4j2
public class CustomOrderService {

	@Autowired
	private CustomOrderRepository customOrderRepository;
	@Autowired
	private UserRepository userRepository;

	// =====================================================
	// 1️⃣ CREATE CUSTOM ORDER
	// =====================================================
	public CustomOrderResponse createOrder(CustomOrderRequest request) {

		log.info("Creating custom order for userId: {}", request.getUserId());

		User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		CustomOrder order = new CustomOrder();
		order.setUser(user); //

		order.setOccasion(request.getOccasion());
		order.setWeight(request.getWeight());
		order.setFlavor(request.getFlavor());
		order.setMessageOnCake(request.getMessageOnCake());
		order.setDesignReference(request.getDesignReference());
		order.setDeliveryDate(request.getDeliveryDate());
		order.setStatus(CustomOrderStatus.PLACED);

		CustomOrder savedOrder = customOrderRepository.save(order);

		return mapToResponse(savedOrder);
	}

	// =====================================================
	// 2️⃣ GET ORDER BY ID
	// =====================================================
	public CustomOrderResponse getOrderById(Long orderId) {

		log.info("Fetching custom order by orderId: {}", orderId);

		CustomOrder order = customOrderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Custom order not found"));

		return mapToResponse(order);
	}

	// =====================================================
	// 3️⃣ GET ALL ORDERS OF USER
	// =====================================================
	public List<CustomOrderResponse> getOrdersByUser(Long userId) {

		log.info("Fetching custom orders for userId: {}", userId);

		return customOrderRepository.findByUserId(userId).stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	// =====================================================
	// 4️⃣ GET ALL ORDERS (ADMIN)
	// =====================================================
	public List<CustomOrderResponse> getAllOrders() {

		log.info("Fetching all custom orders");

		return customOrderRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	// =====================================================
	// 5️⃣ UPDATE ORDER STATUS
	// =====================================================
	public CustomOrderResponse updateOrderStatus(UpdateCustomOrderStatusRequest request) {

		log.info("Updating order status | orderId: {}, status: {}", request.getOrderId(), request.getStatus());

		CustomOrder order = customOrderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new RuntimeException("Custom order not found"));

		order.setStatus(request.getStatus());

		CustomOrder updatedOrder = customOrderRepository.save(order);
		return mapToResponse(updatedOrder);
	}

	// =====================================================
	// 6️⃣ CANCEL ORDER
	// =====================================================
	public void cancelOrder(Long orderId) {

		log.info("Cancelling custom order | orderId: {}", orderId);

		CustomOrder order = customOrderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Custom order not found"));

		order.setStatus(CustomOrderStatus.CANCELLED);
		customOrderRepository.save(order);
	}

	// =====================================================
	// 7️⃣ DELETE ORDER (HARD DELETE)
	// =====================================================
	public void deleteOrder(Long orderId) {

		log.info("Deleting custom order | orderId: {}", orderId);

		if (!customOrderRepository.existsById(orderId)) {
			throw new RuntimeException("Custom order not found");
		}

		customOrderRepository.deleteById(orderId);
	}

	// =====================================================
	// 🔁 ENTITY → RESPONSE MAPPER
	// =====================================================
	private CustomOrderResponse mapToResponse(CustomOrder order) {

		CustomOrderResponse response = new CustomOrderResponse();
		response.setOrderId(order.getId());
		response.setUserId(order.getUser().getId());
		response.setOccasion(order.getOccasion());
		response.setWeight(order.getWeight());
		response.setFlavor(order.getFlavor());
		response.setMessageOnCake(order.getMessageOnCake());
		response.setDesignReference(order.getDesignReference());
		response.setDeliveryDate(order.getDeliveryDate());
		response.setStatus(order.getStatus());
		response.setCreatedAt(order.getCreatedAt());

		return response;
	}
}
