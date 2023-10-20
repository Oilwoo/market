package com.example.market.dto;

import com.example.market.model.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSendDTO {
    private Long id;
    private Double totalPrice;
    private String status;
    // 생성자, 혹은 필요에 따라 추가 메소드들...

    public OrderSendDTO(Orders orders) {
        this.id = orders.getId();
        this.totalPrice = orders.getTotalPrice();
        this.status = orders.getStatus();
    }
}
