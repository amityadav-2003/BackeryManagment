package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderType;
import com.uniquebitehub.ApplicationMain.Enum.PaymentStatus;

import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // PRIMARY KEY
    // ==========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // RELATIONSHIP
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ==========================
    // ORDER FIELDS
    // ==========================
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(
        name = "created_at",
        insertable = false,
        updatable = false
    )
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    // ==========================
    // CHILD TABLES
    // ==========================
    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderItem> orderItems;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL
    )
    private List<Payment> payments;

    // ==========================
    // DEFAULT VALUES
    // ==========================
    @PrePersist
    protected void onCreate() {
        if (this.orderStatus == null) {
            this.orderStatus = OrderStatus.PLACED;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
    }
}
