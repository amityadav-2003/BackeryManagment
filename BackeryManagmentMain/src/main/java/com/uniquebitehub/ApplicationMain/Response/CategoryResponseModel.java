package com.uniquebitehub.ApplicationMain.Response;

import java.util.List;

import com.uniquebitehub.ApplicationMain.Enum.CategoryStatus;

import lombok.Data;

@Data
public class CategoryResponseModel {
	
	 private Long id;
	    private String name;
	    private CategoryStatus status;
	 
	

}
