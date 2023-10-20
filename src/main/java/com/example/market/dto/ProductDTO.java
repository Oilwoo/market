package com.example.market.dto;

// 필요한 import 문들
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
