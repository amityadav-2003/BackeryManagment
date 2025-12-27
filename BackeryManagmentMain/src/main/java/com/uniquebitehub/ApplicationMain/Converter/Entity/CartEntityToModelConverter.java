package com.uniquebitehub.ApplicationMain.Converter.Entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.Cart;
import com.uniquebitehub.ApplicationMain.Entity.CartItem;
import com.uniquebitehub.ApplicationMain.Entity.ProductVariant;
import com.uniquebitehub.ApplicationMain.Response.CartItemResponseModel;
import com.uniquebitehub.ApplicationMain.Response.CartResponseModel;

@Component
public class CartEntityToModelConverter {

    // ================= CART → RESPONSE =================
    public CartResponseModel toResponse(Cart cart) {

        CartResponseModel response = new CartResponseModel();
        response.setCartId(cart.getId());

        List<CartItemResponseModel> itemResponses = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem item : cart.getCartItems()) {

            CartItemResponseModel itemResponse = new CartItemResponseModel();

            itemResponse.setCartItemId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());

            BigDecimal price;

            // Variant handling
            if (item.getProductVariant() != null) {
                ProductVariant variant = item.getProductVariant();
                itemResponse.setVariant(variant.getWeight());
                price = variant.getPrice();
            } else {
                itemResponse.setVariant(null);
                price = item.getProduct().getBasePrice();
            }
            
            // ✅ product image from product_images table
            if (!item.getProduct().getProductImages().isEmpty()) {
            	itemResponse.setImageUrl(
                    item.getProduct()
                        .getProductImages()
                        .get(0)
                        .getImageUrl()
                );
            }

            itemResponse.setPrice(price);
            itemResponse.setQuantity(item.getQuantity());

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            itemResponse.setItemTotal(itemTotal);

            totalAmount = totalAmount.add(itemTotal);
            totalItems += item.getQuantity();

            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);
        response.setTotalAmount(totalAmount);
        response.setTotalItems(totalItems);

        return response;
    }
}
