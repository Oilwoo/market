package com.example.market.service;

import com.example.market.model.Orders;
import com.example.market.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Orders saveOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    public Optional<Orders> getOrder(Long id) {
        return ordersRepository.findById(id);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public List<Orders> getOrdersByUserId(Long userId) {
        return ordersRepository.findByUserId(userId);
    }

    public Orders cancelOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("Invalid order ID: " + orderId)
        );

        if (!order.getStatus().equals(Orders.STATUS_CANCELLED)) {  // prevent canceling an already canceled order
            order.setStatus(Orders.STATUS_CANCELLED);
            ordersRepository.save(order);
        }

        return order;
    }

    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);  // You might want to add some checks or handle exceptions here.
    }


    // Add any other service methods you need for order processing.
}
