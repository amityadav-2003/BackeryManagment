package com.uniquebitehub.ApplicationMain.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.Category;
import com.uniquebitehub.ApplicationMain.Enum.CategoryStatus;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	
	 List<Category> findByStatus(CategoryStatus status);


}
