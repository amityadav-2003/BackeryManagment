package com.uniquebitehub.ApplicationMain.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.CategoryStatus;

@Entity
@Data
@Table(name = "categories")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status = CategoryStatus.ACTIVE;


   

   
}
