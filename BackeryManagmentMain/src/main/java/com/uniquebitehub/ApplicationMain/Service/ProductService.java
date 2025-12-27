package com.uniquebitehub.ApplicationMain.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.uniquebitehub.ApplicationMain.Entity.Category;
import com.uniquebitehub.ApplicationMain.Entity.Product;
import com.uniquebitehub.ApplicationMain.Entity.ProductImage;
import com.uniquebitehub.ApplicationMain.Entity.ProductVariant;
import com.uniquebitehub.ApplicationMain.Enum.ProductStatus;
import com.uniquebitehub.ApplicationMain.Repository.CategoryRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductImageRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductVariantRepository;
import com.uniquebitehub.ApplicationMain.Request.AddProductRequestModel;
import com.uniquebitehub.ApplicationMain.Request.AddProductVariantRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UpdateProductRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UpdateProductVariantRequestModel;
import com.uniquebitehub.ApplicationMain.Response.ProductResponseModel;
import com.uniquebitehub.ApplicationMain.Response.ProductVariantResponseModel;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Autowired
	private ProductVariantRepository productVariantRepository;

	@Autowired
	private FileService fileService;

	// ================= ADD PRODUCT (JSON only) =================
	public ProductResponseModel addProduct(AddProductRequestModel request) {
		log.info("Adding product: {}", request.getName());

		// Validate required fields
		if (!StringUtils.hasText(request.getName())) {
			throw new RuntimeException("Product name is required");
		}
		if (request.getCategoryId() == null) {
			throw new RuntimeException("Category ID is required");
		}
		if (request.getBasePrice() == null || request.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("Valid base price is required");
		}

		// Validate category exists
		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

		// Create product entity
		Product product = new Product();
		product.setCategory(category);
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setBasePrice(request.getBasePrice());
		product.setIsCustomizable(request.getCustomizable() != null ? request.getCustomizable() : false);
		product.setStatus(ProductStatus.ACTIVE);

		// Save product first to get ID
		Product savedProduct = productRepository.save(product);

		// Save product variants if provided
		if (request.getVariants() != null && !request.getVariants().isEmpty()) {
			saveProductVariants(savedProduct, request.getVariants());
		}

		log.info("Product added successfully with ID: {}", savedProduct.getId());
		return mapToResponseModel(savedProduct);
	}

	// ================= ADD PRODUCT WITH MULTIPART FILES =================
	public ProductResponseModel addProduct(AddProductRequestModel request, List<MultipartFile> images) {
		log.info("Adding product with images: {}", request.getName());

		// First, create the product without images
		ProductResponseModel productResponse = addProduct(request);

		// Then save uploaded images if provided
		if (images != null && !images.isEmpty()) {
			Product product = productRepository.findById(productResponse.getId())
					.orElseThrow(() -> new RuntimeException("Product not found after creation"));

			saveProductImagesFromFiles(product, images);
		}

		// Return the complete product with images
		return findById(productResponse.getId());
	}

	// ================= SAVE PRODUCT VARIANTS =================
	private void saveProductVariants(Product product, List<AddProductVariantRequestModel> list) {
		List<ProductVariant> variants = new ArrayList<>();

		for (AddProductVariantRequestModel variantRequest : list) {
			// Validate variant
			if (!StringUtils.hasText(variantRequest.getWeight())) {
				throw new RuntimeException("Variant weight is required");
			}
			if (variantRequest.getPrice() == null || variantRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
				throw new RuntimeException("Valid variant price is required");
			}

			ProductVariant variant = new ProductVariant();
			variant.setProduct(product);
			variant.setWeight(variantRequest.getWeight());
			variant.setPrice(variantRequest.getPrice());
			variants.add(variant);
		}

		productVariantRepository.saveAll(variants);
		log.info("Saved {} variants for product ID: {}", variants.size(), product.getId());
	}

	// ================= SAVE PRODUCT IMAGES FROM FILES =================
	private void saveProductImagesFromFiles(Product product, List<MultipartFile> images) {
		List<ProductImage> productImages = new ArrayList<>();

		for (MultipartFile image : images) {
			if (image != null && !image.isEmpty()) {
				try {
					// Save file to filesystem and get path
					String imagePath = fileService.saveFile(image, "products");

					// Create ProductImage entity
					ProductImage productImage = new ProductImage();
					productImage.setProduct(product);
					productImage.setImageUrl(imagePath);
					productImages.add(productImage);

					log.info("Saved image: {} for product ID: {}", imagePath, product.getId());

				} catch (Exception e) {
					log.error("Failed to save image: {}", image.getOriginalFilename(), e);
					// Continue with other images
				}
			}
		}

		if (!productImages.isEmpty()) {
			productImageRepository.saveAll(productImages);
		}
	}

	// ================= UPDATE PRODUCT =================
	public ProductResponseModel updateProduct(UpdateProductRequestModel request, List<MultipartFile> newImages) {
		log.info("Updating product ID: {}", request.getId());

		if (request.getId() == null) {
			throw new RuntimeException("Product ID is required for update");
		}

		Product product = productRepository.findById(request.getId())
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getId()));

		// Update basic details
		updateProductDetails(product, request);

		// Update variants if provided
		if (request.getVariants() != null) {
			updateProductVariants(product, request.getVariants());
		}

		Product savedProduct = productRepository.save(product);

		// Add new images if provided
		if (newImages != null && !newImages.isEmpty()) {
			saveProductImagesFromFiles(savedProduct, newImages);
		}

		log.info("Product updated successfully: {}", savedProduct.getId());
		return mapToResponseModel(savedProduct);
	}

	// ================= UPDATE PRODUCT DETAILS =================
	private void updateProductDetails(Product product, UpdateProductRequestModel request) {
		if (StringUtils.hasText(request.getName())) {
			product.setName(request.getName());
		}
		if (StringUtils.hasText(request.getDescription())) {
			product.setDescription(request.getDescription());
		}
		if (request.getBasePrice() != null) {
			product.setBasePrice(request.getBasePrice());
		}
		if (request.getCustomizable() != null) {
			product.setIsCustomizable(request.getCustomizable());
		}
		if (request.getCategoryId() != null) {
			Category category = categoryRepository.findById(request.getCategoryId())
					.orElseThrow(() -> new RuntimeException("Category not found"));
			product.setCategory(category);
		}
		if (request.getStatus() != null) {
			product.setStatus(request.getStatus());
		}
	}

	// ================= UPDATE PRODUCT VARIANTS =================

	private void updateProductVariants(Product product, List<UpdateProductVariantRequestModel> list) {
		// Delete existing variants
		productVariantRepository.deleteById(product.getId());

		// Save new variants
		if (!list.isEmpty()) {
			updateProductVariants(product, list);
		}
	}

	// ================= DELETE PRODUCT IMAGE =================
	public void deleteProductImage(Long imageId) {
		log.info("Deleting product image ID: {}", imageId);

		ProductImage image = productImageRepository.findById(imageId)
				.orElseThrow(() -> new RuntimeException("Product image not found with id: " + imageId));

		// Delete physical file
		deletePhysicalFile(image.getImageUrl());

		productImageRepository.deleteById(imageId);
		log.info("Product image deleted successfully: {}", imageId);
	}

	// ================= DELETE PRODUCT =================
	public void deleteProduct(Long id) {
		log.info("Deleting product ID: {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

		// Delete images physically from filesystem
		List<ProductImage> images = productImageRepository.findByProductId(id);
		for (ProductImage image : images) {
			deletePhysicalFile(image.getImageUrl());
		}

		// Note: ON DELETE CASCADE will handle variants and images in database
		productRepository.deleteById(id);
		log.info("Product deleted successfully: {}", id);
	}

	// ================= DELETE PHYSICAL FILE =================
	private void deletePhysicalFile(String filePath) {
		if (StringUtils.hasText(filePath)) {
			try {
				java.io.File file = new java.io.File(filePath);
				if (file.exists() && file.delete()) {
					log.info("Deleted physical file: {}", filePath);
				}
			} catch (Exception e) {
				log.warn("Failed to delete physical file: {}", filePath, e);
			}
		}
	}

	// ================= CHANGE STATUS =================
	public void changeStatus(Long id, ProductStatus status) {
		log.info("Changing status for product ID: {} to {}", id, status);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

		product.setStatus(status);
		productRepository.save(product);

		log.info("Product status updated: {} -> {}", id, status);
	}

	// ================= FIND BY ID =================
	public ProductResponseModel findById(Long id) {
		log.info("Finding product by ID: idd {}", id);

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

		return mapToResponseModel(product);
	}

	// ================= FIND ALL =================
	public List<ProductResponseModel> findAll() {
		log.info("Finding all products");

		List<Product> products = productRepository.findAll();
		return products.stream().map(this::mapToResponseModel).collect(Collectors.toList());
	}

	// ================= FIND ALL ACTIVE =================
	public List<ProductResponseModel> findAllActive() {
		log.info("Finding all active products");

		List<Product> products = productRepository.findByStatus(ProductStatus.ACTIVE);
		return products.stream().map(this::mapToResponseModel).collect(Collectors.toList());
	}

	// ================= MAP ENTITY TO RESPONSE MODEL =================
	private ProductResponseModel mapToResponseModel(Product product) {
		ProductResponseModel response = new ProductResponseModel();

		response.setId(product.getId());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setBasePrice(product.getBasePrice());
		response.setCustomizable(product.isIsCustomizable());
		response.setStatus(product.getStatus());
		response.setCreatedAt(product.getCreatedAt());

		// Category info
		if (product.getCategory() != null) {
			response.setCategoryId(product.getCategory().getId());
			response.setCategoryName(product.getCategory().getName());
		}

		// Get variants
		Optional<ProductVariant> variants = productVariantRepository.findById(product.getId());
		
		List<ProductVariantResponseModel> variantResponses = variants.stream().map(variant -> {
			ProductVariantResponseModel variantResponse = new ProductVariantResponseModel();
			variantResponse.setId(variant.getId());
			variantResponse.setWeight(variant.getWeight());
			variantResponse.setPrice(variant.getPrice());
			return variantResponse;
		}).collect(Collectors.toList());
		response.setVariants(variantResponses);

		 // Get images - MAKE SURE THIS IS BEING CALLED
	    List<ProductImage> images = productImageRepository.findByProductId(product.getId());
	    log.info("Found {} images for product ID: {}", images.size(), product.getId());
	    
	    List<String> imageUrls = images.stream()
	        .map(ProductImage::getImageUrl)
	        .filter(url -> url != null && !url.isEmpty())
	        .collect(Collectors.toList());
	    
	    response.setImageUrls(imageUrls);
	    log.info("Image URLs for product ID {}: {}", product.getId(), imageUrls);

		return response;
	}
}