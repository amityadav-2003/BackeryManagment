package com.uniquebitehub.ApplicationMain.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Entity.OrderItem;
import com.uniquebitehub.ApplicationMain.Entity.Product;
import com.uniquebitehub.ApplicationMain.Entity.ProductVariant;
import com.uniquebitehub.ApplicationMain.Entity.User;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	Optional<OrderItem> findByOrderAndProductAndProductVariant(Order order, Product product, ProductVariant variant);

}
