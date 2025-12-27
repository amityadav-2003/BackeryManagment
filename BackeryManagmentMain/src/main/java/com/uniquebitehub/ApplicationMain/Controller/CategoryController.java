package com.uniquebitehub.ApplicationMain.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniquebitehub.ApplicationMain.Enum.CategoryStatus;
import com.uniquebitehub.ApplicationMain.Request.CategoryRequestModel;
import com.uniquebitehub.ApplicationMain.Response.CategoryResponseModel;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // ================= CREATE =================
    @Operation(summary = "Create Category")
    @PostMapping("/create")
    public RestResponse create(@RequestBody CategoryRequestModel request) {
        LOGGER.info("Create category request received: {}", request.getName());
        try {
            CategoryResponseModel response = categoryService.create(request);
            return RestResponse.build()
                    .withSuccess("Category created successfully", response);
        } catch (Exception e) {
            LOGGER.error("Error while creating category", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= UPDATE =================
    @Operation(summary = "Update Category")
    @PutMapping("/update")
    public RestResponse update(@RequestBody CategoryRequestModel request) {
        LOGGER.info("Update category request received, ID: {}", request.getId());
        try {
            CategoryResponseModel response = categoryService.update(request);
            return RestResponse.build()
                    .withSuccess("Category updated successfully", response);
        } catch (Exception e) {
            LOGGER.error("Error while updating category", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND BY ID =================
    @Operation(summary = "Find Category By ID")
    @GetMapping("/findById")
    public RestResponse findById(@RequestParam Long id) {
        LOGGER.info("Find category by id: {}", id);
        try {
            CategoryResponseModel response = categoryService.findById(id);
            return RestResponse.build()
                    .withSuccess("Category found", response);
        } catch (Exception e) {
            LOGGER.error("Error while fetching category by id", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND ALL =================
    @Operation(summary = "Find All Categories")
    @GetMapping("/findAll")
    public RestResponse findAll() {
        LOGGER.info("Fetching all categories");
        try {
            List<CategoryResponseModel> list = categoryService.findAll();
            return RestResponse.build()
                    .withSuccess("Categories fetched successfully", list);
        } catch (Exception e) {
            LOGGER.error("Error while fetching categories", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= CHANGE STATUS =================
    @Operation(summary = "Change Category Status")
    @PutMapping("/changeStatus")
    public RestResponse changeStatus(
            @RequestParam Long id,
            @RequestParam CategoryStatus status) {

        LOGGER.info("Change status for category id: {}, status: {}", id, status);

        try {
            categoryService.changeStatus(id, status);
            return RestResponse.build()
                    .withSuccess("Category status changed successfully");
        } catch (Exception e) {
            LOGGER.error("Error while changing category status", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }


    // ================= HARD DELETE =================
    @Operation(summary = "Delete Category Permanently")
    @DeleteMapping("/delete")
    public RestResponse delete(@RequestParam Long id) {
        LOGGER.info("Hard delete category id: {}", id);
        try {
            categoryService.delete(id);
            return RestResponse.build()
                    .withSuccess("Category deleted permanently");
        } catch (Exception e) {
            LOGGER.error("Error while deleting category", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }
}
