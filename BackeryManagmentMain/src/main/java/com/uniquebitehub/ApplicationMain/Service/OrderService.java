package com.uniquebitehub.ApplicationMain.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Converter.Entity.OrderEntityToModelConverter;
import com.uniquebitehub.ApplicationMain.Entity.Address;
import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Entity.OrderItem;
import com.uniquebitehub.ApplicationMain.Entity.Product;
import com.uniquebitehub.ApplicationMain.Entity.ProductVariant;
import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderType;
import com.uniquebitehub.ApplicationMain.Enum.PaymentStatus;
import com.uniquebitehub.ApplicationMain.Repository.AddressRepository;
import com.uniquebitehub.ApplicationMain.Repository.OrderItemRepository;
import com.uniquebitehub.ApplicationMain.Repository.OrderRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductVariantRepository;
import com.uniquebitehub.ApplicationMain.Repository.UserRepository;
import com.uniquebitehub.ApplicationMain.Request.AddOrderItemRequest;
import com.uniquebitehub.ApplicationMain.Request.CreateOrderItemRequest;
import com.uniquebitehub.ApplicationMain.Request.CreateOrderRequest;
import com.uniquebitehub.ApplicationMain.Response.OrderAdminResponse;
import com.uniquebitehub.ApplicationMain.Response.OrderResponse;
import com.uniquebitehub.ApplicationMain.Response.OrderTrackingResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductVariantRepository variantRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderEntityToModelConverter orderEntityToModelConverter;

	// ================= CREATE =================
	
	@Transactional
	public OrderResponse createOrder(CreateOrderRequest request) {

	    // 1️⃣ Find User
	    User user = userRepository.findById(request.getUserId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // 2️⃣ Find Address
	    Address address = addressRepository.findById(request.getAddressId())
	            .orElseThrow(() -> new RuntimeException("Address not found"));

	    // 3️⃣ Create Order
	    Order order = new Order();
	    order.setUser(user);
	    order.setAddress(address);                // ✅ IMPORTANT
	    order.setOrderType(request.getOrderType());
	    order.setOrderStatus(OrderStatus.PLACED);
	    order.setPaymentStatus(PaymentStatus.PENDING);
	    order.setDeliveryDate(request.getDeliveryDate());
	    order.setTotalAmount(BigDecimal.ZERO);    // ✅ avoid NOT NULL issue

	    // 4️⃣ Save Order first (ID required for items)
	    Order savedOrder = orderRepository.save(order);

	    BigDecimal total = BigDecimal.ZERO;

	    // 5️⃣ Create Order Items
	    for (CreateOrderItemRequest itemReq : request.getItems()) {

	        Product product = productRepository.findById(itemReq.getProductId())
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        ProductVariant variant = null;
	        BigDecimal price;

	        // ✅ Variant handling (SAFE)
	        if (itemReq.getVariantId() != null) {
	            variant = variantRepository.findById(itemReq.getVariantId())
	                    .orElseThrow(() -> new RuntimeException("Variant not found"));
	            price = variant.getPrice();
	        } else {
	            // If product has variants, variant is mandatory
	            
	            price = product.getBasePrice();
	        }

	        OrderItem item = new OrderItem();
	        item.setOrder(savedOrder);
	        item.setProduct(product);
	        item.setProductVariant(variant);
	        item.setQuantity(itemReq.getQuantity());
	        item.setPrice(price);

	        orderItemRepository.save(item);

	        // Optional but recommended if bidirectional mapping exists
	        savedOrder.getOrderItems().add(item);

	        total = total.add(
	                price.multiply(BigDecimal.valueOf(itemReq.getQuantity()))
	        );
	    }

	    // 6️⃣ Update Total Amount
	    savedOrder.setTotalAmount(total);

	    // No need to save again if persistence context is active
	    // orderRepository.save(savedOrder);

	    // 7️⃣ Convert to Response
	    return orderEntityToModelConverter.convert(savedOrder);
	}


	// ================= GET ORDER BY ID =================
    public OrderResponse getOrderById(Long orderId,Long userId) {
    	
    	Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if user exists (if userId is provided)
        if (userId != null) {
            // Option 1: Check if user exists in database
            boolean userExists = userRepository.existsById(userId);
            if (!userExists) {
                throw new RuntimeException("User not found");
            }
            
            // Option 2: Check if order belongs to the user
            if (!order.getUser().getId().equals(userId)) {
                throw new RuntimeException("Unauthorized access to order");
            }
        }

        return orderEntityToModelConverter.convert(order);
    }

    // ================= USER ORDERS =================
    public List<OrderResponse> getUserOrders(
            Long userId,
            OrderStatus status,
            LocalDate startDate,
            LocalDate endDate) {

        // CORRECTED: Find orders by user ID, not by order ID
        List<Order> orders = orderRepository.findByUserId(userId);
        // OR: orderRepository.findByUserIdOrderByCreatedAtDesc(userId)

        return orders.stream()
                .filter(o -> status == null || o.getOrderStatus() == status)
                .filter(o -> startDate == null || !o.getCreatedAt().toLocalDate().isBefore(startDate))
                .filter(o -> endDate == null || !o.getCreatedAt().toLocalDate().isAfter(endDate))
                .map(order -> orderEntityToModelConverter.convert(order))  // Changed method name for clarity
                .collect(Collectors.toList());
    }

    // ================= CANCEL ORDER =================
    public OrderResponse cancelOrder(Long orderId, String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order already cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return orderEntityToModelConverter.convert(savedOrder);
    }

    // ================= TRACK ORDER =================
    public OrderTrackingResponse trackOrder(Long orderId, Long userId) {
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if the order belongs to the user (for security)
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to order");
        }
        
        OrderTrackingResponse response = new OrderTrackingResponse();
        response.setOrderId(order.getId());
        response.setCurrentStatus(order.getOrderStatus());
        
        // Set appropriate message based on order status
        String message = getTrackingMessage(order.getOrderStatus());
        response.setMessage(message);
        
        return response;
    }
    private String getTrackingMessage(OrderStatus status) {
        switch (status) {
            case PLACED:
                return "Your order has been placed and is awaiting confirmation.";
            case CONFIRMED:
                return "Your order has been confirmed and is being prepared.";
            case BAKING:
                return "Your delicious treats are being freshly baked!";
          
            case OUT_FOR_DELIVERY:
                return "Your order is out for delivery with our delivery partner.";
            case DELIVERED:
                return "Your order has been successfully delivered. Enjoy!";
            case CANCELLED:
                return "Your order has been cancelled.";
         
            default:
                return "Your order is being processed.";
        }
    }
    // ================= ADMIN : GET ALL ORDERS =================
    public List<OrderAdminResponse> getAllOrders(
            OrderStatus status,
            PaymentStatus paymentStatus,
            OrderType orderType,
            LocalDate from,
            LocalDate to) {

        return orderRepository.findAll().stream()
                .filter(o -> status == null || o.getOrderStatus() == status)
                .filter(o -> paymentStatus == null || o.getPaymentStatus() == paymentStatus)
                .filter(o -> orderType == null || o.getOrderType() == orderType)
                .filter(o -> from == null || !o.getCreatedAt().toLocalDate().isBefore(from))
                .filter(o -> to == null || !o.getCreatedAt().toLocalDate().isAfter(to))
                .map(order -> {
                    OrderAdminResponse res = new OrderAdminResponse();
                    res.setOrderId(order.getId());
                    res.setUserId(order.getUser().getId());
                    res.setTotalAmount(order.getTotalAmount());
                    res.setOrderStatus(order.getOrderStatus());
                    res.setPaymentStatus(order.getPaymentStatus());
                    res.setOrderType(order.getOrderType());
                    res.setCreatedAt(order.getCreatedAt());
                    return res;
                })
                .collect(Collectors.toList());
    }

    // ================= ADMIN : UPDATE ORDER STATUS =================
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
        Order savedOrder = orderRepository.save(order);

        return orderEntityToModelConverter.convert(savedOrder);
    }

    // ================= ADMIN : UPDATE DELIVERY DATE =================
    public OrderResponse updateDeliveryDate(Long orderId, LocalDate date) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setDeliveryDate(date);
        Order savedOrder = orderRepository.save(order);

        return orderEntityToModelConverter.convert(savedOrder);
    }

    public OrderResponse addOrderItem(Long orderId, Long userId, AddOrderItemRequest request) {

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Validate Order Ownership
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to modify this order");
        }

        // 3️⃣ Validate Order Status
        if (!order.getOrderStatus().isEditable()) {
            throw new RuntimeException("Order cannot be modified in current state");
        }

        // 4️⃣ Fetch Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 5️⃣ Fetch Variant (if present)
        ProductVariant variant = null;
        BigDecimal price;

        if (request.getVariantId() != null) {
            variant = variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));
            price = variant.getPrice();
        } else {
            price = product.getBasePrice();
        }

        // 6️⃣ Check if same item already exists (product + variant)
        OrderItem orderItem = orderItemRepository
                .findByOrderAndProductAndProductVariant(order, product, variant)
                .orElse(null);

        if (orderItem != null) {
            // Update quantity
            orderItem.setQuantity(orderItem.getQuantity() + request.getQuantity());
        } else {
            // Create new item
            orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(variant);
            orderItem.setQuantity(request.getQuantity());
            orderItem.setPrice(price);
        }

        orderItemRepository.save(orderItem);

        // 7️⃣ Recalculate total amount safely
        BigDecimal total = order.getOrderItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // 8️⃣ Return response
        return orderEntityToModelConverter.convert(savedOrder);
    }

    public OrderResponse removeOrderItem(Long orderId, Long userId, Long itemId) {

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Validate ownership
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to modify this order");
        }

        // 3️⃣ Validate order status
        if (!order.getOrderStatus().isEditable()) {
            throw new RuntimeException("Order cannot be modified in current state");
        }

        // 4️⃣ Fetch Order Item
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // 5️⃣ Ensure item belongs to order
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new RuntimeException("Order item does not belong to this order");
        }

        // 6️⃣ Delete item
        orderItemRepository.delete(orderItem);

        // 7️⃣ Recalculate total amount
        BigDecimal total = order.getOrderItems().stream()
                .filter(i -> !i.getId().equals(itemId))
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // 8️⃣ Return response
        return orderEntityToModelConverter.convert(savedOrder);
    }

    public OrderResponse updateItemQuantity(Long orderId, Long itemId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Validate order status
        if (!order.getOrderStatus().isEditable()) {
            throw new RuntimeException("Order cannot be modified in current state");
        }

        // 3️⃣ Fetch Order Item
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // 4️⃣ Validate item belongs to order
        if (!orderItem.getOrder().getId().equals(orderId)) {
            throw new RuntimeException("Order item does not belong to this order");
        }

        // 5️⃣ Update quantity
        orderItem.setQuantity(quantity);
        orderItemRepository.save(orderItem);

        // 6️⃣ Recalculate total
        BigDecimal total = order.getOrderItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // 7️⃣ Return response
        return orderEntityToModelConverter.convert(savedOrder);
    }


}
