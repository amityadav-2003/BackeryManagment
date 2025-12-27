package com.uniquebitehub.ApplicationMain.Response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseModel {
	
	private Long id;

    private String name;

    private String email;

    private String phone;

    private Long roleId;

    private String RoleName;
    
    private String status;   // ACTIVE / BLOCKED

    private LocalDateTime createdAt;


}
