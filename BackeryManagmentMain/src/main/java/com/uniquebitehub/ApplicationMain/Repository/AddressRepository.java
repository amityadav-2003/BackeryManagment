package com.uniquebitehub.ApplicationMain.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.Address;
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    List<Address> findByUserId(Long userId);
	
}
