package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;

@Data
public class UserSignupRequestModel {

	
	//@NotBlank(message = "Name is required")
    private String name;

    //@Email(message = "Invalid email format")
    //@NotBlank(message = "Email is required")
    private String email;

    private String phone;

    //@NotBlank(message = "Password is required")
    //@Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
