package com.uniquebitehub.ApplicationMain.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long>{

	 List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
	    
	    // Using @Query annotation for complex filtering
	    @Query("SELECT o FROM Order o WHERE " +
	           "o.user.id = :userId " +
	           "AND (:status IS NULL OR o.orderStatus = :status) " +
	           "AND (:startDate IS NULL OR o.createdAt >= :startDate) " +
	           "AND (:endDate IS NULL OR o.createdAt <= :endDate) " +
	           "ORDER BY o.createdAt DESC")
	    List<Order> findByUserIdAndFilters(
	            @Param("userId") Long userId,
	            @Param("status") OrderStatus status,
	            @Param("startDate") LocalDateTime startDate,
	            @Param("endDate") LocalDateTime endDate);
	    
	    // OR using JPA Specifications for dynamic queries
	    List<Order> findAll(Specification<Order> spec);

		List<Order> findByUserId(Long userId);
		
		long countByCreatedAtAfter(LocalDateTime date);

		@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o")
		double getTotalRevenueAll();

		@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createdAt >= :date")
		double getTotalRevenueAfter(LocalDateTime date);

	    
}
