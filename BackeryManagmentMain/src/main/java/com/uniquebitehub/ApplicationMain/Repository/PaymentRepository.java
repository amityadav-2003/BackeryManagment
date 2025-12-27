package com.uniquebitehub.ApplicationMain.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniquebitehub.ApplicationMain.Entity.Order;
import com.uniquebitehub.ApplicationMain.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

	

	 Optional<Payment> findTopByOrder_IdOrderByIdDesc(Long orderId);
	

}
