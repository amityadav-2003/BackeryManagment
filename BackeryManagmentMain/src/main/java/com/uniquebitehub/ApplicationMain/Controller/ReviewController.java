package com.uniquebitehub.ApplicationMain.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.uniquebitehub.ApplicationMain.Request.ReviewRequestModel;
import com.uniquebitehub.ApplicationMain.Response.ReviewResponseModel;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    // ================= CREATE =================
    @Operation(summary = "Create Review")
    @PostMapping("/create")
    public RestResponse create(@RequestBody ReviewRequestModel request) {
        try {
            return RestResponse.build()
                    .withSuccess("Review created successfully", reviewService.create(request));
        } catch (Exception e) {
            LOGGER.error("Error creating review", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= UPDATE =================
    @Operation(summary = "Update Review")
    @PutMapping("/update")
    public RestResponse update(@RequestBody ReviewRequestModel request) {
        try {
            return RestResponse.build()
                    .withSuccess("Review updated successfully", reviewService.update(request));
        } catch (Exception e) {
            LOGGER.error("Error updating review", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND BY ID =================
    @Operation(summary = "Find Review By Id")
    @GetMapping("/findById")
    public RestResponse findById(@RequestParam Long id) {
        try {
            return RestResponse.build()
                    .withSuccess("Review found", reviewService.findById(id));
        } catch (Exception e) {
            LOGGER.error("Error finding review", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND BY PRODUCT =================
    @Operation(summary = "Find Reviews By Product")
    @GetMapping("/findByProduct")
    public RestResponse findByProduct(@RequestParam Long productId) {
        try {
            return RestResponse.build()
                    .withSuccess("Product reviews fetched", reviewService.findByProduct(productId));
        } catch (Exception e) {
            LOGGER.error("Error fetching product reviews", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND BY USER =================
    @Operation(summary = "Find Reviews By User")
    @GetMapping("/findByUser")
    public RestResponse findByUser(@RequestParam Long userId) {
        try {
            return RestResponse.build()
                    .withSuccess("User reviews fetched", reviewService.findByUser(userId));
        } catch (Exception e) {
            LOGGER.error("Error fetching user reviews", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND ALL =================
    @Operation(summary = "Find All Reviews")
    @GetMapping("/findAll")
    public RestResponse findAll() {
        try {
            return RestResponse.build()
                    .withSuccess("All reviews fetched", reviewService.findAll());
        } catch (Exception e) {
            LOGGER.error("Error fetching reviews", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= DELETE =================
    @Operation(summary = "Delete Review")
    @DeleteMapping("/delete")
    public RestResponse delete(@RequestParam Long id) {
        try {
            reviewService.delete(id);
            return RestResponse.build().withSuccess("Review deleted successfully");
        } catch (Exception e) {
            LOGGER.error("Error deleting review", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }
}
