package com.uniquebitehub.ApplicationMain.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.ProductVariant;
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

}
