package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;

@Data
public class UserUpdateRequestModel {

	private Long id;

	private String name;
	private String email;

	private String phone;

	// Image path (set after upload)
	private String profilePhotoPath;
}
