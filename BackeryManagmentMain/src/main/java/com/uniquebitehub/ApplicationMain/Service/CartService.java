package com.uniquebitehub.ApplicationMain.Service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquebitehub.ApplicationMain.Converter.Entity.CartEntityToModelConverter;
import com.uniquebitehub.ApplicationMain.Entity.Cart;
import com.uniquebitehub.ApplicationMain.Entity.CartItem;
import com.uniquebitehub.ApplicationMain.Entity.Product;
import com.uniquebitehub.ApplicationMain.Entity.ProductVariant;
import com.uniquebitehub.ApplicationMain.Repository.CartItemRepository;
import com.uniquebitehub.ApplicationMain.Repository.CartRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductRepository;
import com.uniquebitehub.ApplicationMain.Repository.ProductVariantRepository;
import com.uniquebitehub.ApplicationMain.Request.AddCartItemRequestModel;
import com.uniquebitehub.ApplicationMain.Request.CartItemUpdateRequestModel;
import com.uniquebitehub.ApplicationMain.Response.CartResponseModel;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private CartEntityToModelConverter cartConverter;

    // ================= ADD TO CART =================
    public CartResponseModel addToCart(AddCartItemRequestModel request) {

        // 1️⃣ Find or Create Cart
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setId(request.getUserId());
                    return cartRepository.save(newCart);
                });

        // 2️⃣ Validate Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3️⃣ Validate Variant (optional)
        ProductVariant variant = null;
        if (request.getVariantId() != null) {
            variant = variantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));
        }

        // 4️⃣ Check if item already exists
        Optional<CartItem> existingItem =
                cartItemRepository.findByCart_IdAndProduct_IdAndProductVariant_Id(
                        cart.getId(), product.getId(), request.getVariantId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setProductVariant(variant);
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        return cartConverter.toResponse(cart);
    }

    // ================= UPDATE CART ITEM =================
    public CartResponseModel updateCartItem(CartItemUpdateRequestModel request) {

        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (request.getQuantity() <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        return cartConverter.toResponse(item.getCart());
    }

    // ================= REMOVE ITEM =================
    public void removeItem(Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(item);
    }

    // ================= VIEW CART =================
    public CartResponseModel viewCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        return cartConverter.toResponse(cart);
    }

    // ================= CART TOTAL =================
    public BigDecimal getCartTotal(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {

            BigDecimal price;

            if (item.getProductVariant() != null) {
                price = item.getProductVariant().getPrice();
            } else {
                price = item.getProduct().getBasePrice();
            }

            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return total;
    }

    // ================= CLEAR CART =================
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteById(cart.getId());
    }
}
