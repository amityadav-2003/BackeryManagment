package com.uniquebitehub.ApplicationMain.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.ProductImage;
@Repository
public interface ProductImageRepository  extends JpaRepository<ProductImage, Long>{
	   List<ProductImage> findByProductId(Long productId);
	    void deleteByProductId(Long productId);
	
}
