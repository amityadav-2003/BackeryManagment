package com.uniquebitehub.ApplicationMain.Response;

import lombok.Data;

@Data
public class AddressResponseModel {

    private Long id;
    private Long userId;
    private String address;
    private String city;
    private String pincode;
}
