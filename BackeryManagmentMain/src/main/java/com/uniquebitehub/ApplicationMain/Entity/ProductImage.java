package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "product_images")
public class ProductImage implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================
    // PRIMARY KEY
    // ==========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // IMAGE URL
    // ==========================
    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    // ==========================
    // RELATIONSHIP (ONLY ONE)
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

   
}
