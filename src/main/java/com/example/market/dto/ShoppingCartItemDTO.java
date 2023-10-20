package com.example.market.dto;

// 필요한 import 문들
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShoppingCartItemDTO {
    private Long cartId;
    private Long productId;
    private int quantity;
}