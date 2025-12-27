package com.uniquebitehub.ApplicationMain.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uniquebitehub.ApplicationMain.Entity.CartItem;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

//    Optional<CartItem> findByCartIdAndProductIdAndVariantId(
//            Long cartId, Long productId, Long variantId);
//    
    Optional<CartItem> findByCart_IdAndProduct_IdAndProductVariant_Id(
            Long cartId,
            Long productId,
            Long variantId
    );

}
