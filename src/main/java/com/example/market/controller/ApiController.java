package com.example.market.controller;

import com.example.market.dto.OrderSendDTO;
import com.example.market.dto.ShoppingCartItemDTO;
import com.example.market.dto.UserDTO;
import com.example.market.model.*;
import com.example.market.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final OrdersService ordersService;
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartItemService shoppingCartItemService;
    @Autowired
    public ApiController(UserService userService, OrdersService ordersService, ProductService productService, ShoppingCartService shoppingCartService, ShoppingCartItemService shoppingCartItemService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartItemService = shoppingCartItemService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(userDTO.getPassword()); // for real applications you must encrypt this password
        newUser.setEmail(userDTO.getEmail());

        userService.registerUser(newUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response); // You could also return the created User object here
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        // In a real application, Spring Security handles this part.
        // We are simplifying by just checking if the user exists with the provided credentials.

        User user = userService.findByUsername(userDTO.getUsername());

        if (user != null && user.getPassword().equals(userDTO.getPassword())) { // never use plain-text comparisons in production
            Map<String, String> response = new HashMap<>();
            response.put("message", "로그인성공");

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "사용자이름이나 비밀번호가 잘못 되었습니다.");

            return ResponseEntity.badRequest().body(response);
        }
    }

    // 상품 목록 조회
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 장바구니에 상품 추가
    @PostMapping("/cart/item")
    public ResponseEntity<ShoppingCartItem> addProductToCart(@RequestBody ShoppingCartItemDTO itemDTO) {
        ShoppingCartItem item = shoppingCartItemService.addItemToCart(itemDTO);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    // 장바구니의 모든 아이템을 조회합니다.
    @GetMapping("/cart/item/{cartId}")
    public ResponseEntity<List<ShoppingCartItem>> getAllItemsInCart(@PathVariable Long cartId) {
        ShoppingCart cart = shoppingCartService.getCart(cartId);
        List<ShoppingCartItem> items = cart.getItems();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // 장바구니에서 상품 제거
    @GetMapping("/cart/item/delete/{itemId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable Long itemId) {
        ShoppingCartItem item = shoppingCartItemService.getItem(itemId).orElse(null);
        if(item == null) {
            throw new IllegalArgumentException("장바구니에 상품이 존재하지 않습니다.");
        }
        shoppingCartItemService.deleteItem(itemId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "장바구니에서 상품이 성공적으로 제거되었습니다!");
        return ResponseEntity.ok(response);
    }

    // 장바구니의 모든 아이템을 결제합니다.
    @GetMapping("/cart/checkout/{cartId}")
    public ResponseEntity<?> checkoutCart(@PathVariable Long cartId) {
        Orders orders = shoppingCartService.checkout(cartId);

        // Orders 엔티티를 DTO로 변환
        OrderSendDTO ordersDTO = new OrderSendDTO(orders);

        // DTO 객체를 리턴
        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    // 해당유저의 상품 주문목록을 조회
    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderSendDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<Orders> ordersList = ordersService.getOrdersByUserId(userId);

        if (ordersList.isEmpty()) {
            throw new IllegalArgumentException("No orders found for user: " + userId);
        }

        List<OrderSendDTO> dtoList = ordersList.stream()
                .map(OrderSendDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("orders/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Orders cancelledOrder = ordersService.cancelOrder(orderId);
            return ResponseEntity.ok(new OrderSendDTO(cancelledOrder));  // or return only the status or a message
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // handle other exceptions, possibly returning HttpStatus.INTERNAL_SERVER_ERROR
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while cancelling the order.");
        }
    }




}
