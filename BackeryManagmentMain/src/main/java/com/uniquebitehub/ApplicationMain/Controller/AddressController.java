package com.uniquebitehub.ApplicationMain.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniquebitehub.ApplicationMain.Request.AddressRequestModel;
import com.uniquebitehub.ApplicationMain.Response.AddressResponseModel;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.AddressService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/address")
public class AddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    // ================= CREATE =================
    @Operation(summary = "Create Address")
    @PostMapping("/create")
    public RestResponse create(@RequestBody AddressRequestModel request) {
        LOGGER.info("Create address request received for userId: {}", request.getUserId());
        try {
            AddressResponseModel response = addressService.create(request);
            return RestResponse.build()
                    .withSuccess("Address created successfully", response);
        } catch (Exception e) {
            LOGGER.error("Error while creating address", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= UPDATE =================
    @Operation(summary = "Update Address")
    @PutMapping("/update")
    public RestResponse update(@RequestBody AddressRequestModel request) {
        LOGGER.info("Update address request received, ID: {}", request.getId());
        try {
            AddressResponseModel response = addressService.update(request);
            return RestResponse.build()
                    .withSuccess("Address updated successfully", response);
        } catch (Exception e) {
            LOGGER.error("Error while updating address", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND BY ID =================
    @Operation(summary = "Find Address By ID")
    @GetMapping("/findById")
    public RestResponse findById(@RequestParam Long id) {
        LOGGER.info("Find address by id: {}", id);
        try {
            AddressResponseModel response = addressService.findById(id);
            return RestResponse.build()
                    .withSuccess("Address found", response);
        } catch (Exception e) {
            LOGGER.error("Error while fetching address by id", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND ALL =================
    @Operation(summary = "Find All Addresses")
    @GetMapping("/findAll")
    public RestResponse findAll() {
        LOGGER.info("Fetching all addresses");
        try {
            List<AddressResponseModel> list = addressService.findAll();
            return RestResponse.build()
                    .withSuccess("Addresses fetched successfully", list);
        } catch (Exception e) {
            LOGGER.error("Error while fetching addresses", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= FIND BY USER =================
    @Operation(summary = "Find Addresses By User")
    @GetMapping("/findByUser")
    public RestResponse findByUser(@RequestParam Long userId) {
        LOGGER.info("Find addresses by userId: {}", userId);
        try {
            List<AddressResponseModel> list = addressService.findByUserId(userId);
            return RestResponse.build()
                    .withSuccess("User addresses fetched successfully", list);
        } catch (Exception e) {
            LOGGER.error("Error while fetching user addresses", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= HARD DELETE =================
    @Operation(summary = "Delete Address Permanently")
    @DeleteMapping("/delete")
    public RestResponse delete(@RequestParam Long id) {
        LOGGER.info("Hard delete address id: {}", id);
        try {
            addressService.delete(id);
            return RestResponse.build()
                    .withSuccess("Address deleted permanently");
        } catch (Exception e) {
            LOGGER.error("Error while deleting address", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }
}
