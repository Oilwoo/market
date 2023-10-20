package com.example.market.service;


import com.example.market.model.Product;
import com.example.market.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        // You can add business logic here before saving a product
        return productRepository.save(product);
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        // Check if the product exists before updating it
        if (product.getId() != null && productRepository.existsById(product.getId())) {
            return productRepository.save(product);
        }
        // Handle the case where the product does not exist or throw an exception
        return null; // Or you could throw an appropriate exception
    }

    public void deleteProduct(Long id) {
        // Before deleting, you can add checks to see if the product exists
        productRepository.deleteById(id);
    }

    // any other additional methods to handle business operations related to products
}
