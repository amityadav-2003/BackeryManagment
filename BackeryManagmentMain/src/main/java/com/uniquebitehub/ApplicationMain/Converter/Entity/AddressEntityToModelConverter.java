package com.uniquebitehub.ApplicationMain.Converter.Entity;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.Address;
import com.uniquebitehub.ApplicationMain.Response.AddressResponseModel;

@Component
public class AddressEntityToModelConverter {
	
	public AddressResponseModel convert(Address address) {
		
		
		

		    AddressResponseModel model = new AddressResponseModel();
		    model.setId(address.getId());
		    model.setUserId(address.getUser().getId());
		    model.setAddress(address.getAddress());
		    model.setCity(address.getCity());
		    model.setPincode(address.getPincode());

		    return model;
		}
		
	

}
