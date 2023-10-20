package com.example.market.repository;

// src/main/java/com/yourpackage/repository/ProductRepository.java

import com.example.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Here, you can declare query methods, for example, finding products by name.
    // List<Product> findByNameContaining(String name);
}
