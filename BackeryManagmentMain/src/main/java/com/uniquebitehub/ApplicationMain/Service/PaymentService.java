package com.uniquebitehub.ApplicationMain.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Entity.Payment;
import com.uniquebitehub.ApplicationMain.Enum.PaymentMethod;
import com.uniquebitehub.ApplicationMain.Enum.PaymentStatus;
import com.uniquebitehub.ApplicationMain.Repository.OrderRepository;
import com.uniquebitehub.ApplicationMain.Repository.PaymentRepository;
import com.uniquebitehub.ApplicationMain.Request.PaymentRequest;
import com.uniquebitehub.ApplicationMain.Response.PaymentResponse;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    // ================= PROCESS PAYMENT =================
    public PaymentResponse processPayment(PaymentRequest request) {

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Check if already PAID
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Payment already completed for this order");
        }

        // 3️⃣ Create Payment Entity
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId(request.getTransactionId());

        // 4️⃣ Handle payment logic
        if (request.getPaymentMethod() == PaymentMethod.CASH) {
            payment.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            // UPI / CARD assumed success for now
        
            payment.setPaidAt(LocalDateTime.now());
        }

        Payment savedPayment = paymentRepository.save(payment);

        // 5️⃣ Update Order Payment Status
        if (savedPayment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            order.setPaymentStatus(PaymentStatus.PENDING);
        }

        orderRepository.save(order);

        // 6️⃣ Convert to Response
        return mapToResponse(savedPayment);
    }

    // ================= GET PAYMENT STATUS =================
    public PaymentResponse getPaymentStatus(Long orderId) {

        Payment payment = paymentRepository.findTopByOrder_IdOrderByIdDesc(orderId)
                .orElseThrow(() -> new RuntimeException("No payment found for order ID: " + orderId));
        
        return mapToResponse(payment);
    }

    // ================= ENTITY → RESPONSE =================
    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setPaidAt(payment.getPaidAt());

        return response;
    }
}
