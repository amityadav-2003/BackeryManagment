package com.uniquebitehub.ApplicationMain.Converter.Model;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Request.UserSignupRequestModel;
import com.uniquebitehub.ApplicationMain.Request.UserUpdateRequestModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserModelToEntityConverter {

	public User getSaveConvert(UserSignupRequestModel usersignupRequestModel) {

		User user = new User();
		user.setName(usersignupRequestModel.getName());
		user.setEmail(usersignupRequestModel.getEmail());
		user.setPhone(usersignupRequestModel.getPhone());
		user.setPassword(usersignupRequestModel.getPassword());
					return user;
	}

	  public User getUpdateConvert(UserUpdateRequestModel request, User user) {

	        // Update only if value is present (NULL SAFE)

	        if (request.getName() != null && !request.getName().isBlank()) {
	            user.setName(request.getName());
	        }
	        if (request.getEmail() != null && !request.getEmail().isBlank()) {
	            user.setEmail(request.getEmail());
	        }

	        if (request.getPhone() != null && !request.getPhone().isBlank()) {
	            user.setPhone(request.getPhone());
	        }

	        // profilePhoto handled in service (multipart logic)

	        return user;
	    }
}
