package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.ProductStatus;

@Entity
@Data
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // PRIMARY KEY
    // ==========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // BASIC FIELDS
    // ==========================
    @Column(nullable = false, length = 150)
    private String name;

    @Lob
    private String description;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "is_customizable")
    private boolean IsCustomizable;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ==========================
    // RELATIONSHIPS
    // ==========================

    // 🔥 ONLY THIS — NO categoryId field
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> productVariants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    // ==========================
    // AUTO TIMESTAMP
    // ==========================
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

	
	


    // ==========================
    // GETTERS & SETTERS
    // ==========================
   
}
