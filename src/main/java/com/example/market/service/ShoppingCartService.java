package com.example.market.service;

import com.example.market.model.*;
import com.example.market.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Autowired
public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository,OrdersRepository ordersRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public ShoppingCart saveCart(ShoppingCart cart) {
        return shoppingCartRepository.save(cart);
    }

    public ShoppingCart getCart(Long id) {
        return shoppingCartRepository.findById(id).orElse(null);
    }

    public List<ShoppingCart> getAllCarts() {
        return shoppingCartRepository.findAll();
    }

    public void deleteCart(Long id) {
        shoppingCartRepository.deleteById(id);  // Consider adding checks or exception handling.
    }

    @Transactional  // 이 메서드는 트랜잭션 범위에서 실행되어야 함
    public Orders checkout(Long cartId) {
        // 장바구니 조회
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 장바구니 ID"));

        // 새 주문 생성
        Orders order = new Orders();
        User user = cart.getUser();
        order.setUser(user);
        order.setStatus("ORDERED");//주문완료

        // 주문 아이템 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        // 장바구니에 담긴 상품들 체크
        for (ShoppingCartItem cartItem : cart.getItems()) {
            // 아이템 재고 체크
            int stock = cartItem.getProduct().getStockQuantity();
            if (stock < cartItem.getQuantity()) {
                throw new IllegalArgumentException("상품의 재고가 없습니다. " + cartItem.getProduct().getName());
            }

            // 재고수량 반영
            cartItem.getProduct().setStockQuantity(stock - cartItem.getQuantity());
            productRepository.save(cartItem.getProduct());

            // 주문 아이템 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);  // Set the parent order

            orderItems.add(orderItem);  // Add the new OrderItem to the list
        }

        // Link items to the order and set the total price
        order.setItems(orderItems);
        order.setTotalPrice(
                orderItems.stream()
                        .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                        .sum()
        );

        // Save the order along with its items
        Orders savedOrders = ordersRepository.save(order);

        // 장바구니의 ShoppingCartItems를 삭제합니다. 이는 데이터베이스에서 해당 항목들을 제거합니다.
        shoppingCartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear(); // 장바구니 내의 아이템 리스트 비우기
        // 둘다 적용되는지 확인?

        // 장바구니 상태 업데이트 (이제 비어 있음)
        shoppingCartRepository.save(cart);

        return savedOrders;  // 생성된 주문 반환
    }
}
