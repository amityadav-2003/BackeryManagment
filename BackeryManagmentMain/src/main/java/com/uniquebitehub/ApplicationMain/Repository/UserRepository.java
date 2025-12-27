package com.uniquebitehub.ApplicationMain.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	 boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	 @Query("""
		        SELECT u FROM User u 
		        WHERE u.email = :emailOrPhone 
		           OR u.phone = :emailOrPhone
		    """)
		    User findByEmailOrPhone(@Param("emailOrPhone") String emailOrPhone);
	 
	 
	 long countByCreatedAtAfter(LocalDateTime date);


}
