package com.example.market.service;

import com.example.market.dto.ShoppingCartItemDTO;
import com.example.market.exception.InsufficientStockException;
import com.example.market.model.Product;
import com.example.market.model.ShoppingCart;
import com.example.market.model.ShoppingCartItem;
import com.example.market.repository.ProductRepository;
import com.example.market.repository.ShoppingCartItemRepository;
import com.example.market.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartItemService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ShoppingCartItemService(ShoppingCartItemRepository shoppingCartItemRepository, ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }

    public ShoppingCartItem saveItem(ShoppingCartItem item) {
        return shoppingCartItemRepository.save(item);
    }

    public Optional<ShoppingCartItem> getItem(Long id) {
        return shoppingCartItemRepository.findById(id);
    }

    public List<ShoppingCartItem> getAllItems() {
        return shoppingCartItemRepository.findAll();
    }

    public void deleteItem(Long id) {
        shoppingCartItemRepository.deleteById(id);  // You might want to add some checks or handle exceptions here.
    }

    @Transactional
    public ShoppingCartItem addItemToCart(ShoppingCartItemDTO itemDTO) throws InsufficientStockException {
        Long cartId = itemDTO.getCartId();
        Long productId = itemDTO.getProductId();
        int quantity = itemDTO.getQuantity();

        // 장바구니 검즘
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if(cart == null) throw new IllegalArgumentException("유효하지 장바구니 ID입니다. " + cartId);

        // 제품이 존재하는지 확인하고 재고가 충분한지 확인합니다.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 제품 ID: " + productId));

        if(product.getStockQuantity() < quantity) {
            throw new InsufficientStockException("제품 ID에 대한 재고 부족: " + productId);
        }

        // 제품이 이미 장바구니에 있는지 확인하여 수량을 조정하거나 새 항목으로 추가합니다.
        Optional<ShoppingCartItem> optionalCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (optionalCartItem.isPresent()) {
            ShoppingCartItem existingItem = optionalCartItem.get();
            //수량추가
            existingItem.setQuantity(existingItem.getQuantity() + quantity);

            if(product.getStockQuantity() < quantity) {
                throw new InsufficientStockException("제품 ID에 대한 재고 부족: " + productId);
            }

            return shoppingCartItemRepository.save(existingItem);
        } else {
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setQuantity(quantity);
            newItem.setCartId(cart.getId());
            newItem.setProduct(product);

            return shoppingCartItemRepository.save(newItem);
        }
    }
}