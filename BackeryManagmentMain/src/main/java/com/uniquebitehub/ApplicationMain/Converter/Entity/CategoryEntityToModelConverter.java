package com.uniquebitehub.ApplicationMain.Converter.Entity;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.Category;
import com.uniquebitehub.ApplicationMain.Response.CategoryResponseModel;


@Component
public class CategoryEntityToModelConverter {

	  public CategoryResponseModel convert(Category category) {

	        if (category == null) {
	            return null;
	        }

	        CategoryResponseModel response = new CategoryResponseModel();
	        response.setId(category.getId());
	        response.setName(category.getName());
	        response.setStatus(category.getStatus());

	      
	        return response;
	    }

}
