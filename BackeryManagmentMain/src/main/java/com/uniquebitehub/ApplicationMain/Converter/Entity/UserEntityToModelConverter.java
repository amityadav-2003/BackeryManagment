package com.uniquebitehub.ApplicationMain.Converter.Entity;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Response.UserResponseModel;


@Component
public class UserEntityToModelConverter {

	public UserResponseModel getFindByIdConvert(User savedUser) {
		UserResponseModel response = new UserResponseModel();
		response.setId(savedUser.getId());
		response.setName(savedUser.getName());
		response.setEmail(savedUser.getEmail());
		response.setPhone(savedUser.getPhone());
		response.setRoleId(savedUser.getRole().getId());
		response.setRoleName(savedUser.getRole().getRoleName());
		response.setStatus(savedUser.getStatus().name());

		response.setCreatedAt(savedUser.getCreatedAt());

		return response;
	}

}
