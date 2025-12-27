package com.uniquebitehub.ApplicationMain.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long>{
	
	Optional<Role> findByRoleName(String roleName);

}
