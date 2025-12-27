package com.uniquebitehub.ApplicationMain.Controller;

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

import com.uniquebitehub.ApplicationMain.Request.AddCartItemRequestModel;
import com.uniquebitehub.ApplicationMain.Request.CartItemUpdateRequestModel;
import com.uniquebitehub.ApplicationMain.Response.CartResponseModel;
import com.uniquebitehub.ApplicationMain.RestResponse.RestResponse;
import com.uniquebitehub.ApplicationMain.Service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/cart")
// @CrossOrigin(origins = "*")
public class CartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    // ================= ADD TO CART =================
    @Operation(summary = "Add item to cart")
    @PostMapping("/add")
    public RestResponse addToCart(@RequestBody AddCartItemRequestModel request) {

        LOGGER.info("Add to cart | userId: {}, productId: {}", 
                request.getUserId(), request.getProductId());
        try {
            CartResponseModel response = cartService.addToCart(request);
            return RestResponse.build()
                    .withSuccess("Item added to cart successfully", response);
        } catch (Exception e) {
            LOGGER.error("Add to cart failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= UPDATE CART ITEM =================
    @Operation(summary = "Update cart item quantity")
    @PutMapping("/update")
    public RestResponse updateCartItem(@RequestBody CartItemUpdateRequestModel request) {

        LOGGER.info("Update cart item | cartItemId: {}", request.getCartItemId());
        try {
            CartResponseModel response = cartService.updateCartItem(request);
            return RestResponse.build()
                    .withSuccess("Cart item updated successfully", response);
        } catch (Exception e) {
            LOGGER.error("Update cart item failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= REMOVE CART ITEM =================
    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/removeItem")
    public RestResponse removeItem(@RequestParam Long cartItemId) {

        LOGGER.info("Remove cart item | cartItemId: {}", cartItemId);
        try {
            cartService.removeItem(cartItemId);
            return RestResponse.build()
                    .withSuccess("Item removed from cart successfully");
        } catch (Exception e) {
            LOGGER.error("Remove cart item failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= VIEW CART =================
    @Operation(summary = "View user cart")
    @GetMapping("/view")
    public RestResponse viewCart(@RequestParam Long userId) {

        LOGGER.info("View cart | userId: {}", userId);
        try {
            CartResponseModel response = cartService.viewCart(userId);
            return RestResponse.build()
                    .withSuccess("Cart fetched successfully", response);
        } catch (Exception e) {
            LOGGER.error("View cart failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= CART TOTAL =================
    @Operation(summary = "Get cart total")
    @GetMapping("/total")
    public RestResponse cartTotal(@RequestParam Long userId) {

        LOGGER.info("Get cart total | userId: {}", userId);
        try {
            return RestResponse.build()
                    .withSuccess("Cart total calculated",
                            cartService.getCartTotal(userId));
        } catch (Exception e) {
            LOGGER.error("Cart total calculation failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }

    // ================= CLEAR CART =================
    @Operation(summary = "Clear cart")
    @DeleteMapping("/clear")
    public RestResponse clearCart(@RequestParam Long userId) {

        LOGGER.info("Clear cart | userId: {}", userId);
        try {
            cartService.clearCart(userId);
            return RestResponse.build()
                    .withSuccess("Cart cleared successfully");
        } catch (Exception e) {
            LOGGER.error("Clear cart failed", e);
            return RestResponse.build().withError(e.getMessage());
        }
    }
}
