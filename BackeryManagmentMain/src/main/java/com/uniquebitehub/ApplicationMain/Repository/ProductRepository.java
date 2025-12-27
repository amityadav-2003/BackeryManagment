package com.uniquebitehub.ApplicationMain.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.uniquebitehub.ApplicationMain.Entity.Product;
import com.uniquebitehub.ApplicationMain.Enum.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	//Collection<MultipartFile> findByStatus(ProductStatus active);
	
	  // Find all products by status
    List<Product> findByStatus(ProductStatus status);

}
