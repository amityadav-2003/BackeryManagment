package com.uniquebitehub.ApplicationMain.Controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquebitehub.ApplicationMain.Request.UserLoginRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UserSignupRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UserUpdateRequestModel;
import com.uniquebitehub.ApplicationMain.Response.UserResponseModel;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.FileService;
import com.uniquebitehub.ApplicationMain.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {

	 
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ObjectMapper objectMapper;

	public UserController() {
		LOGGER.info("UserController: Object Created");
	}

	// USER SIGNUP
	@Operation(summary = "User Signup Endpoint", description = "Allows new users to register.")
	@RequestMapping(method = RequestMethod.POST, value = "/signup")
	public RestResponse signup(@RequestBody UserSignupRequestModel usersignupRequestModel) {
		LOGGER.info("Saving user data with email: {}", usersignupRequestModel.getEmail());
		try {
			UserResponseModel userResponseModel = userService.signup(usersignupRequestModel);
			return RestResponse.build().withSuccess("User created successfully", userResponseModel);
		} catch (Exception e) {
			LOGGER.error("Failed to save user: {}", e.getMessage(), e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// USERS LOGIN
//	@Operation(summary = "User Login Endpoint", description = "Authenticates a user based on Email/Phone and Password.")
//	@RequestMapping(method = RequestMethod.POST, value = "/login")
//	public RestResponse login(@RequestBody UserLoginRequestModel userLoginRequestModel) {
//		try {
//			LOGGER.info("User login attempt with Email/Phone: {}", userLoginRequestModel.getEmail(),
//					userLoginRequestModel.getPhone());
//			UserResponseModel userResponseModel = userService.login(userLoginRequestModel);
//			return RestResponse.build().withSuccess("Login successful", userResponseModel);
//		} catch (Exception e) {
//			LOGGER.error("Failed to login user: {}", e.getMessage(), e);
//			return RestResponse.build().withError(e.getMessage());
//		}
//	}

//   @Operation(summary = "User Login", description = "Login using email/phone and password")
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public RestResponse login(@RequestBody UserLoginRequestModel request) {
		try {
			UserResponseModel response = userService.login(request);
			return RestResponse.build().withSuccess("Login successful", response);
		} catch (Exception e) {
			LOGGER.error("Login failed", e);
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// =========================
	// 3️⃣ FIND USER BY ID
	// =========================
	@Operation(summary = "Find User By ID")
	@RequestMapping(method = RequestMethod.GET, value = "/findById")
	public RestResponse findById(@RequestParam("id") Long id) {
		try {
			UserResponseModel response = userService.findById(id);
			return RestResponse.build().withSuccess("User found", response);
		} catch (Exception e) {
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// =========================
	// 4️⃣ FIND ALL USERS (ADMIN)
	// =========================
	@Operation(summary = "Find All Users")
	@RequestMapping(method = RequestMethod.GET, value = "/findAll")
	public RestResponse findAll() {
		return RestResponse.build().withSuccess("Users fetched", userService.findAll());
	}

	// =========================
	// 5️⃣ UPDATE USER PROFILE (WITH IMAGE)
	// =========================
	@Operation(summary = "Update User Profile With Image")
	@RequestMapping(method = RequestMethod.PUT, value = "/updateProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RestResponse updateProfile(@RequestParam("userUpdateRequestModelJson") String json,
			@RequestParam(value = "profilePhotoFile", required = false) MultipartFile file) throws IOException {

		String filePath = null;

		try {
			UserUpdateRequestModel request = objectMapper.readValue(json, UserUpdateRequestModel.class);

			if (file != null && !file.isEmpty()) {

				if (file.getSize() > fileService.getFileMaxSize()) {
					return RestResponse.build().withError("File size exceeds limit");
				}

				String extension = FilenameUtils.getExtension(file.getOriginalFilename());
				filePath = fileService.saveFile(file.getBytes(), "users", extension);
				request.setProfilePhotoPath("uploads/users/" + filePath);
			}

			UserResponseModel response = userService.updateProfile(request);
			return RestResponse.build().withSuccess("Profile updated", response);

		} catch (Exception e) {
			if (filePath != null) {
				FileUtils.deleteQuietly(new File("uploads/users/" + filePath));
			}
			return RestResponse.build().withError(e.getMessage());
		}
	}

	// =========================
	// 6️⃣ CHANGE USER STATUS
	// =========================
	@Operation(summary = "Change User Status (ACTIVE/BLOCKED)")
	@RequestMapping(method = RequestMethod.PUT, value = "/changeStatus")
	public RestResponse changeStatus(@RequestParam("userId") Long userId) {
		userService.changeStatus(userId);
		return RestResponse.build().withSuccess("User status changed");
	}

	// =========================
	// 7️⃣ SOFT DELETE USER
	// =========================
	@Operation(summary = "Soft Delete User")
	@RequestMapping(method = RequestMethod.PUT, value = "/softDelete")
	public RestResponse softDelete(@RequestParam("userId") Long userId) {
		userService.softDeleteById(userId);
		return RestResponse.build().withSuccess("User soft deleted");
	}

	// =========================
	// 8️⃣ HARD DELETE USER
	// =========================
	@Operation(summary = "Hard Delete User")
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteById")
	public RestResponse deleteById(@RequestParam("id") Long id) {
		userService.deleteById(id);
		return RestResponse.build().withSuccess("User deleted permanently");
	}

	// =========================
	// 9️⃣ CHECK EMAIL EXISTS
	// =========================
	@Operation(summary = "Check Email Exists")
	@RequestMapping(method = RequestMethod.GET, value = "/exists/email")
	public RestResponse checkEmail(@RequestParam("email") String email) {
		return RestResponse.build().withSuccess("Result", userService.existsByEmail(email));
	}

	// =========================
	// 🔟 CHECK PHONE EXISTS
	// =========================
	@Operation(summary = "Check Phone Exists")
	@RequestMapping(method = RequestMethod.GET, value = "/exists/phone")
	public RestResponse checkPhone(@RequestParam("phone") String phone) {
		return RestResponse.build().withSuccess("Result", userService.existsByPhone(phone));
	}

}