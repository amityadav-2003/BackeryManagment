package com.uniquebitehub.ApplicationMain.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniquebitehub.ApplicationMain.Entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);
}
