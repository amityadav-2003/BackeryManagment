package com.uniquebitehub.ApplicationMain.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquebitehub.ApplicationMain.Enum.ProductStatus;
import com.uniquebitehub.ApplicationMain.Request.AddProductRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UpdateProductRequestModel;
import com.uniquebitehub.ApplicationMain.Response.ProductResponseModel;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.FileService;
import com.uniquebitehub.ApplicationMain.Service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/product")
//@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ObjectMapper objectMapper;

//    // ================= ADD PRODUCT WITH SINGLE IMAGE =================
//    @Operation(summary = "Add Product with Single Image")
//    @PostMapping(value = "/addWithPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public RestResponse addProductWithPhoto(
//            @RequestParam("product") String productJson,
//            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
//        
//        String savedImagePath = null;
//        
//        try {
//            // Validate JSON input
//            if (!StringUtils.hasText(productJson)) {
//                return RestResponse.build().withError("Product data JSON is required");
//            }
//
//            LOGGER.info("Adding product with single image");
//
//            // Convert JSON to Request Model
//            AddProductRequestModel requestModel = objectMapper.readValue(productJson, AddProductRequestModel.class);
//
//            LOGGER.info("Product name: {}", requestModel.getName());
//
//            // Validate required fields
//            if (!StringUtils.hasText(requestModel.getName())) {
//                return RestResponse.build().withError("Product name is required");
//            }
//            if (requestModel.getCategoryId() == null) {
//                return RestResponse.build().withError("Category ID is required");
//            }
//
//            // Handle image upload if provided
//            if (image != null && !image.isEmpty()) {
//                LOGGER.info("Processing image: {} ({} bytes)", 
//                        image.getOriginalFilename(), 
//                        image.getSize());
//
//                // File Size Check
//                long maxSize = fileService.getFileMaxSize();
//                if (image.getSize() > maxSize) {
//                    return RestResponse.build()
//                            .withError("File size cannot exceed " + (maxSize / (1024 * 1024)) + " MB");
//                }
//
//                // Create Directory if Not Exists
//                File uploadDir = new File("ProductImages");
//                if (!uploadDir.exists()) {
//                    uploadDir.mkdirs();
//                }
//
//                // Generate unique filename
//                String originalFilename = image.getOriginalFilename();
//                String fileExtension = FilenameUtils.getExtension(originalFilename);
//                String uniqueFilename = System.currentTimeMillis() + "_" + 
//                        (StringUtils.hasText(fileExtension) ? "." + fileExtension : ".jpg");
//
//                // Save File
//                File destinationFile = new File(uploadDir, uniqueFilename);
//                image.transferTo(destinationFile);
//                savedImagePath = destinationFile.getAbsolutePath();
//
//                LOGGER.info("Image saved at: {}", savedImagePath);
////                requestModel.setImageUrl(savedImagePath);
//            }
//
//            // Add product via service
//            ProductResponseModel responseModel = productService.addProduct(requestModel);
//            
//            return RestResponse.build()
//                    .withSuccess("Product added successfully")
//                    .withData(responseModel)
//                    .withStatusCode("PRODUCT_CREATED");
//
//        } catch (Exception e) {
//            // Cleanup uploaded file if error occurs
//            if (savedImagePath != null) {
//                try {
//                    File fileToDelete = new File(savedImagePath);
//                    if (fileToDelete.exists()) {
//                        FileUtils.delete(fileToDelete);
//                        LOGGER.info("Cleaned up file: {}", savedImagePath);
//                    }
//                } catch (Exception cleanupEx) {
//                    LOGGER.warn("Failed to cleanup file: {}", savedImagePath, cleanupEx);
//                }
//            }
//            
//            LOGGER.error("Failed to add product due to: {}", e.getMessage(), e);
//            return RestResponse.build()
//                    .withError(e.getMessage())
//                    .withStatusCode("PRODUCT_CREATION_FAILED");
//        }
//    }

    // ================= ADD PRODUCT WITH MULTIPLE IMAGES =================
    @Operation(summary = "Add Product with Multiple Images")
    @PostMapping(value = "/addWithMultipleImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResponse addProductWithMultipleImages(
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {

        List<String> savedImagePaths = new ArrayList<>();

        try {
            // Validate JSON input
            if (!StringUtils.hasText(productJson)) {
                return RestResponse.build().withError("Product data JSON is required");
            }

            LOGGER.info("Adding product with multiple images");

            // Convert JSON to Request Model
            AddProductRequestModel requestModel = objectMapper.readValue(productJson, AddProductRequestModel.class);

            LOGGER.info("Product name: {}", requestModel.getName());

            // Validate required fields
            if (!StringUtils.hasText(requestModel.getName())) {
                return RestResponse.build().withError("Product name is required");
            }
            if (requestModel.getCategoryId() == null) {
                return RestResponse.build().withError("Category ID is required");
            }

            // Handle multiple image uploads if provided
            if (images != null && !images.isEmpty()) {
                LOGGER.info("Processing {} image(s)", images.size());

                List<String> imageUrls = new ArrayList<>();

                for (MultipartFile imageFile : images) {
                    if (imageFile != null && !imageFile.isEmpty()) {
                        try {
                            LOGGER.info("Processing image: {} ({} bytes)", 
                                    imageFile.getOriginalFilename(), 
                                    imageFile.getSize());

                            // File Size Check
                            long maxSize = fileService.getFileMaxSize();
                            if (imageFile.getSize() > maxSize) {
                                throw new RuntimeException(
                                        "File size cannot exceed " + (maxSize / (1024 * 1024)) + " MB");
                            }

                            // Create Directory if Not Exists
                            File uploadDir = new File("ProductImages");
                            if (!uploadDir.exists()) {
                                uploadDir.mkdirs();
                            }

                            // Generate unique filename
                            String originalFilename = imageFile.getOriginalFilename();
                            String fileExtension = FilenameUtils.getExtension(originalFilename);
                            String uniqueFilename = System.currentTimeMillis() + "_" + 
                                    (StringUtils.hasText(fileExtension) ? "." + fileExtension : ".jpg");

                            // Save File
                            File destinationFile = new File(uploadDir, uniqueFilename);
                            imageFile.transferTo(destinationFile);
                            String imagePath = destinationFile.getAbsolutePath();

                            imageUrls.add(imagePath);
                            savedImagePaths.add(imagePath);
                            LOGGER.info("Image saved: {}", imagePath);

                        } catch (Exception fileEx) {
                            LOGGER.error("Failed to save image: {}", imageFile.getOriginalFilename(), fileEx);
                            // Continue with other images
                        }
                    }
                }

//                // Set main image (first one) for backward compatibility
//                if (!imageUrls.isEmpty()) {
//                    requestModel.setImageUrl(imageUrls.get(0));
//                }
                // Set all image URLs if your model supports it
                // requestModel.setAllImageUrls(imageUrls);
            }

            // Add product via service
            ProductResponseModel responseModel = productService.addProduct(requestModel);

            return RestResponse.build()
                    .withSuccess("Product added successfully")
                    .withData(responseModel)
                    .withStatusCode("PRODUCT_CREATED");

        } catch (Exception e) {
            // Cleanup uploaded files if error occurs
            if (!savedImagePaths.isEmpty()) {
                for (String imagePath : savedImagePaths) {
                    try {
                        File fileToDelete = new File(imagePath);
                        if (fileToDelete.exists()) {
                            FileUtils.delete(fileToDelete);
                            LOGGER.info("Cleaned up file: {}", imagePath);
                        }
                    } catch (Exception cleanupEx) {
                        LOGGER.warn("Failed to cleanup file: {}", imagePath, cleanupEx);
                    }
                }
            }

            LOGGER.error("Failed to add product due to: {}", e.getMessage(), e);
            return RestResponse.build()
                    .withError(e.getMessage())
                    .withStatusCode("PRODUCT_CREATION_FAILED");
        }
    }

    // ================= UPDATE PRODUCT =================
    @Operation(summary = "Update Product")
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResponse updateProduct(
            @RequestParam("product") String productJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {

        LOGGER.info("Updating product");
        try {
            // Convert JSON to Request Model
            UpdateProductRequestModel request = objectMapper.readValue(productJson, UpdateProductRequestModel.class);
            
            LOGGER.info("Updating product ID: {}", request.getId());
            
            ProductResponseModel response = productService.updateProduct(request, images);
            return RestResponse.build()
                    .withSuccess("Product updated successfully")
                    .withData(response);
                    
        } catch (Exception e) {
            LOGGER.error("Update product failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= DELETE PRODUCT =================
    @Operation(summary = "Delete Product with Images & Variants")
    @DeleteMapping("/delete")
    public RestResponse deleteProduct(@RequestParam Long id) {
        LOGGER.info("Deleting product ID: {}", id);
        try {
            productService.deleteProduct(id);
            return RestResponse.build()
                    .withSuccess("Product deleted successfully")
                    .withStatusCode("PRODUCT_DELETED");
        } catch (Exception e) {
            LOGGER.error("Delete product failed", e);
            return RestResponse.build()
                    .withError(e.getMessage())
                    .withStatusCode("DELETE_FAILED");
        }
    }

    // ================= CHANGE STATUS =================
    @Operation(summary = "Change Product Status")
    @PutMapping("/changeStatus")
    public RestResponse changeStatus(@RequestParam Long id, @RequestParam ProductStatus status) {
        LOGGER.info("Changing status for product ID: {} to {}", id, status);
        try {
            productService.changeStatus(id, status);
            return RestResponse.build()
                    .withSuccess("Product status updated")
                    .withStatusCode("STATUS_UPDATED");
        } catch (Exception e) {
            LOGGER.error("Status update failed", e);
            return RestResponse.build()
                    .withError(e.getMessage())
                    .withStatusCode("STATUS_UPDATE_FAILED");
        }
    }

    // ================= FIND BY ID =================
    @Operation(summary = "Find Product By ID")
    @GetMapping("/findById")
    public RestResponse findById(@RequestParam Long id) {
        LOGGER.info("Finding product by ID: {}", id);
        try {
            ProductResponseModel product = productService.findById(id);
            return RestResponse.build()
                    .withSuccess("Product found")
                    .withData(product)
                    .withStatusCode("PRODUCT_FOUND");
        } catch (Exception e) {
            LOGGER.error("Find product by ID failed", e);
            return RestResponse.build()
                    .withError(e.getMessage())
                    .withStatusCode("PRODUCT_NOT_FOUND");
        }
    }

    // ================= FIND ALL =================
    @Operation(summary = "Find All Products")
    @GetMapping("/findAll")
    public RestResponse findAll() {
        LOGGER.info("Finding all products");
        try {
            List<ProductResponseModel> list = productService.findAll();
            return RestResponse.build()
                    .withSuccess("All products retrieved")
                    .withData(list)
                    .withStatusCode("PRODUCTS_FOUND");
        } catch (Exception e) {
            LOGGER.error("Find all products failed", e);
            return RestResponse.build()
                    .withError(e.getMessage())
                    .withStatusCode("RETRIEVE_FAILED");
        }
    }

    // ================= FIND ALL ACTIVE =================
    @Operation(summary = "Find All Active Products")
    @GetMapping("/active")
    public RestResponse findAllActive() {
        LOGGER.info("Finding all active products");
        try {
            List<ProductResponseModel> list = productService.findAllActive();
            return RestResponse.build()
                    .withSuccess("Active products retrieved")
                    .withData(list)
                    .withStatusCode("ACTIVE_PRODUCTS_FOUND");
        } catch (Exception e) {
            LOGGER.error("Find all active products failed", e);
            return RestResponse.build()
                    .withError(e.getMessage())
                    .withStatusCode("RETRIEVE_FAILED");
        }
    }
}