package com.uniquebitehub.ApplicationMain.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Converter.Entity.ReviewEntityToModelConverter;
import com.uniquebitehub.ApplicationMain.Entity.Product;
import com.uniquebitehub.ApplicationMain.Entity.Review;
import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Repository.ProductRepository;
import com.uniquebitehub.ApplicationMain.Repository.ReviewRepository;
import com.uniquebitehub.ApplicationMain.Repository.UserRepository;
import com.uniquebitehub.ApplicationMain.Request.ReviewRequestModel;
import com.uniquebitehub.ApplicationMain.Response.ReviewResponseModel;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewEntityToModelConverter reviewConverter;

    // ================= CREATE =================
    public ReviewResponseModel create(ReviewRequestModel request) {

        // fetch user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // fetch product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUser(user);          // ✅ correct
        review.setProduct(product);    // ✅ correct
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewConverter.convert(reviewRepository.save(review));
    }


    // ================= UPDATE =================
    public ReviewResponseModel update(ReviewRequestModel request) {

        Review review = reviewRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewConverter.convert(reviewRepository.save(review));
    }

    // ================= FIND BY ID =================
    public ReviewResponseModel findById(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        return reviewConverter.convert(review);
    }

    // ================= FIND BY PRODUCT =================
    public List<ReviewResponseModel> findByProduct(Long productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (reviews.isEmpty()) return Collections.emptyList();

        return reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList());
    }

    // ================= FIND BY USER =================
    public List<ReviewResponseModel> findByUser(Long userId) {

        List<Review> reviews = reviewRepository.findByUserId(userId);

        if (reviews.isEmpty()) return Collections.emptyList();

        return reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList());
    }

    // ================= FIND ALL =================
    public List<ReviewResponseModel> findAll() {

        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) return Collections.emptyList();

        return reviews.stream()
                .map(reviewConverter::convert)
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
