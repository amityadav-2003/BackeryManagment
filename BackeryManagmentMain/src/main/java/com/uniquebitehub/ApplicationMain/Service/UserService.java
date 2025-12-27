package com.uniquebitehub.ApplicationMain.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.cj.log.Log;
import com.uniquebitehub.ApplicationMain.Converter.Entity.UserEntityToModelConverter;
import com.uniquebitehub.ApplicationMain.Converter.Model.UserModelToEntityConverter;
import com.uniquebitehub.ApplicationMain.Entity.Role;
import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Enum.UserStatus;
import com.uniquebitehub.ApplicationMain.Repository.RoleRepository;
import com.uniquebitehub.ApplicationMain.Repository.UserRepository;
import com.uniquebitehub.ApplicationMain.Request.UserLoginRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UserSignupRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UserUpdateRequestModel;
import com.uniquebitehub.ApplicationMain.Response.UserResponseModel;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Transactional
@Service
@Log4j2
public class UserService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserModelToEntityConverter userModelToEntityConverter;
	@Autowired
	private UserEntityToModelConverter userEntityToModelConverter;

	// USER SIGNUP
	public UserResponseModel signup(UserSignupRequestModel usersignupRequestModel) throws Exception {

		// 1️⃣ Email duplicate check
		if (userRepository.existsByEmail(usersignupRequestModel.getEmail())) {
			throw new RuntimeException("Email already registered");
		}

		// 2️⃣ Phone duplicate check (only if phone provided)
		if (usersignupRequestModel.getPhone() != null
				&& userRepository.existsByPhone(usersignupRequestModel.getPhone())) {
			throw new RuntimeException("Phone already registered");
		}

		// 3️⃣ Convert Request Model → Entity
		User user = userModelToEntityConverter.getSaveConvert(usersignupRequestModel);

		// 6️⃣ Assign CUSTOMER role
		Role role = roleRepository.findByRoleName("CUSTOMER")
				.orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));
		user.setRole(role);

		// 7️⃣ Save user
		User savedUser = userRepository.save(user);

		// 8️⃣ Convert Entity → Response Model
		return userEntityToModelConverter.getFindByIdConvert(savedUser);
	}
	// USER LOGIN
		public UserResponseModel login(UserLoginRequestModel userLoginRequestModel) throws Exception {
			
			User user = userRepository.findByEmailOrPhone(userLoginRequestModel.getEmailOrPhone());
			if (user == null) {
				throw new Exception("Invalid credentials. Please check your username and password and try again.");
			}
			if (!user.getPassword().equals(userLoginRequestModel.getPassword())) {
				throw new Exception("Incorrect email/phone and password.");
			}
			return userEntityToModelConverter.getFindByIdConvert(user);
	}
		 public UserResponseModel findById(Long id) {

		        User user = userRepository.findById(id)
		                .orElseThrow(() -> new RuntimeException("User not found"));

		        return userEntityToModelConverter.getFindByIdConvert(user);
		    }

		    // =====================================================
		    // 4️⃣ FIND ALL USERS
		    // =====================================================
		    public List<UserResponseModel> findAll() {

		        return userRepository.findAll()
		                .stream()
		                .map(userEntityToModelConverter::getFindByIdConvert)
		                .collect(Collectors.toList());
		    }

		    // =====================================================
		    // 5️⃣ UPDATE USER PROFILE
		    // =====================================================
		    public UserResponseModel updateProfile(UserUpdateRequestModel request) {

		    	  log.info("Updating user profile for userId: {}", request.getId());
		        User user = userRepository.findById(request.getId())
		                .orElseThrow(() -> new RuntimeException("User not found"));

		        userModelToEntityConverter.getUpdateConvert(request, user);

		        if (request.getProfilePhotoPath() != null) {
		            user.setProfilePhoto(request.getProfilePhotoPath());
		        }

		        User updatedUser = userRepository.save(user);

		        return userEntityToModelConverter.getFindByIdConvert(updatedUser);
		    }

		    // =====================================================
		    // 6️⃣ CHANGE USER STATUS (ACTIVE ↔ BLOCKED)
		    // =====================================================
		    public void changeStatus(Long userId) {

		        User user = userRepository.findById(userId)
		                .orElseThrow(() -> new RuntimeException("User not found"));

		        if (user.getStatus() == UserStatus.ACTIVE) {
		            user.setStatus(UserStatus.BLOCKED);
		        } else {
		            user.setStatus(UserStatus.ACTIVE);
		        }

		        userRepository.save(user);
		    }

		    // =====================================================
		    // 7️⃣ SOFT DELETE USER
		    // =====================================================
		    public void softDeleteById(Long userId) {

		        User user = userRepository.findById(userId)
		                .orElseThrow(() -> new RuntimeException("User not found"));

		        user.setStatus(UserStatus.BLOCKED);
		        userRepository.save(user);
		    }

		    // =====================================================
		    // 8️⃣ HARD DELETE USER
		    // =====================================================
		    public void deleteById(Long id) {

		        if (!userRepository.existsById(id)) {
		            throw new RuntimeException("User not found");
		        }

		        userRepository.deleteById(id);
		    }

		    // =====================================================
		    // 9️⃣ CHECK EMAIL EXISTS
		    // =====================================================
		    public boolean existsByEmail(String email) {
		        return userRepository.existsByEmail(email);
		    }

		    // =====================================================
		    // 🔟 CHECK PHONE EXISTS
		    // =====================================================
		    public boolean existsByPhone(String phone) {
		        return userRepository.existsByPhone(phone);
		    }


}