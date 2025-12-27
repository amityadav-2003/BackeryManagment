package com.uniquebitehub.ApplicationMain.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Converter.Entity.AddressEntityToModelConverter;
import com.uniquebitehub.ApplicationMain.Entity.Address;
import com.uniquebitehub.ApplicationMain.Entity.User;
import com.uniquebitehub.ApplicationMain.Repository.AddressRepository;
import com.uniquebitehub.ApplicationMain.Repository.UserRepository;
import com.uniquebitehub.ApplicationMain.Request.AddressRequestModel;
import com.uniquebitehub.ApplicationMain.Response.AddressResponseModel;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressEntityToModelConverter addressEntityToModelConverter;

    // ================= CREATE ADDRESS =================
    public AddressResponseModel create(AddressRequestModel request) {

    	
        Address address = new Address();
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setUser(user);
    //    address.setUser(request.getUserId());
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setPincode(request.getPincode());

        Address savedAddress = addressRepository.save(address);

        return addressEntityToModelConverter.convert(savedAddress);
    }

    // ================= UPDATE ADDRESS =================
    public AddressResponseModel update(AddressRequestModel request) {

        Address address = addressRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setPincode(request.getPincode());

        Address updatedAddress = addressRepository.save(address);

        return addressEntityToModelConverter.convert(updatedAddress);
    }

    // ================= FIND BY ID =================
    public AddressResponseModel findById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        return addressEntityToModelConverter.convert(address);
    }

    // ================= FIND BY USER =================
    public List<AddressResponseModel> findByUserId(Long userId) {

        List<Address> addresses = addressRepository.findByUserId(userId);

        if (addresses.isEmpty()) {
            return Collections.emptyList();
        }

        return addresses.stream()
                .map(addressEntityToModelConverter::convert)
                .collect(Collectors.toList());
    }

    // ================= FIND ALL =================
    public List<AddressResponseModel> findAll() {

        List<Address> addresses = addressRepository.findAll();

        if (addresses.isEmpty()) {
            return Collections.emptyList();
        }

        return addresses.stream()
                .map(addressEntityToModelConverter::convert)
                .collect(Collectors.toList());
    }

    // ================= HARD DELETE =================
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
