package com.uniquebitehub.ApplicationMain.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniquebitehub.ApplicationMain.Entity.CustomOrder;
import com.uniquebitehub.ApplicationMain.Enum.OrderStatus;

public interface CustomOrderRepository  extends JpaRepository<CustomOrder, Long>{
	
	

    List<CustomOrder> findByUserId(Long userId);

    List<CustomOrder> findByStatus(OrderStatus status);
    
    List<CustomOrder> findByDeliveryDate(LocalDate deliveryDate);
    List<CustomOrder> findByUserIdAndStatus(Long userId, OrderStatus status);

}
