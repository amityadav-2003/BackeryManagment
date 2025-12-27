package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uniquebitehub.ApplicationMain.Enum.CustomOrderStatus;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;

@Data
@Entity
@Table(name = "custom_orders") // ✅ EXACT DB TABLE NAME
public class CustomOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // PRIMARY KEY
    // ==========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // RELATIONSHIP (MATCHES user_id BIGINT)
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // ==========================
    // ORDER DETAILS
    // ==========================
    @Column(nullable = true)
    private String occasion;

    @Column(nullable = true)
    private String weight;

    @Column(nullable = true)
    private String flavor;

    @Column(name = "message_on_cake")
    private String messageOnCake;

    @Column(name = "design_reference")
    private String designReference;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CustomOrderStatus status;

    // ==========================
    // TIMESTAMP (DB GENERATED)
    // ==========================
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

	

    // ==========================
    // DEFAULT STATUS SAFETY
    // ==========================
   

	
}
