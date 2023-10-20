package com.example.market.service;

import com.example.market.model.OrderItem;
import com.example.market.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem saveOrderItem(OrderItem item) {
        return orderItemRepository.save(item);
    }

    public Optional<OrderItem> getOrderItem(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);  // Consider adding checks or exception handling.
    }

    // Additional methods for business rules related to order items can be added here.
}

