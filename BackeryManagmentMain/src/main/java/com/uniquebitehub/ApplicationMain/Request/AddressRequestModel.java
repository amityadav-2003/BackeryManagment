package com.uniquebitehub.ApplicationMain.Request;
import lombok.Data;

@Data
public class AddressRequestModel {
	
	  private Long id;
    private Long userId;
    private String address;
    private String city;
    private String pincode;
}
