package com.uniquebitehub.ApplicationMain.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Converter.Entity.CategoryEntityToModelConverter;
import com.uniquebitehub.ApplicationMain.Entity.Category;
import com.uniquebitehub.ApplicationMain.Enum.CategoryStatus;
import com.uniquebitehub.ApplicationMain.Repository.CategoryRepository;
import com.uniquebitehub.ApplicationMain.Request.CategoryRequestModel;
import com.uniquebitehub.ApplicationMain.Response.CategoryResponseModel;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryEntityToModelConverter categoryEntityToModelConverter;

    // CREATE CATEGORY
 // CREATE CATEGORY
    public CategoryResponseModel create(CategoryRequestModel request) {

        Category category = new Category();
        category.setName(request.getName());
        category.setStatus(CategoryStatus.ACTIVE);

        Category savedCategory = categoryRepository.save(category);

        return categoryEntityToModelConverter.convert(savedCategory);
    }


    // UPDATE CATEGORY
 // UPDATE CATEGORY
    public CategoryResponseModel update(CategoryRequestModel request) {

        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());

        Category updatedCategory = categoryRepository.save(category);

        return categoryEntityToModelConverter.convert(updatedCategory);
    }


    // GET BY ID
    public CategoryResponseModel findById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        return categoryEntityToModelConverter.convert(category);
    }

  

    // SOFT DELETE
    public void softDelete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setStatus(CategoryStatus.INACTIVE);
        categoryRepository.save(category);
    }

    // HARD DELETE
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

   

	 public CategoryResponseModel changeStatus(Long id, CategoryStatus status) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setStatus(status);
        Category cat =  categoryRepository.save(category);
        return categoryEntityToModelConverter.convert(cat);
        
    }

	 public List<CategoryResponseModel> findAll() {

		    List<Category> categories = categoryRepository.findAll();

		    if (categories.isEmpty()) {
		        return Collections.emptyList();
		    }

		    return categories.stream()
		            .map(categoryEntityToModelConverter::convert)
		            .collect(Collectors.toList());
		}

}
