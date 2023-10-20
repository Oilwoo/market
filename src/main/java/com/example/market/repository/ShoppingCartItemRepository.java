package com.example.market.repository;

import com.example.market.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    // Custom query methods for shopping cart items can be specified here if needed.
}
