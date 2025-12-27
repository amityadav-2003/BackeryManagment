package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // PRIMARY KEY
    // ==========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // FIELDS
    // ==========================
    @Column(nullable = false)
    private String weight;   // 0.5kg, 1kg

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // ==========================
    // RELATIONSHIP
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // ==========================
    // CONSTRUCTOR
    // ==========================
    public ProductVariant() {
    }

    // ==========================
    // GETTERS & SETTERS
    // ==========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
