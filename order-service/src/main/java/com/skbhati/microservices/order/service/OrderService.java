package com.skbhati.microservices.order.service;

import com.skbhati.microservices.order.dto.OrderRequest;
import com.skbhati.microservices.order.dto.OrderResponse;
import com.skbhati.microservices.order.entity.Order;
import com.skbhati.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse getOrder(Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(order -> new OrderResponse(order.getId(),order.getOrderNumber(), order.getSkuCode(), order.getQuantity(), order.getPrice()))
                .orElse(null);
    }

    public OrderResponse placeOrder(OrderRequest order) {
        Order orderEntity = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .price(order.price())
                .quantity(order.quantity())
                .skuCode(order.skuCode())
                .build();

        Order newOrder = orderRepository.save(orderEntity);
        return new OrderResponse(
                newOrder.getId(),
                newOrder.getOrderNumber(),
                newOrder.getSkuCode(),
                newOrder.getQuantity(),
                newOrder.getPrice()
        );
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(order -> new OrderResponse(order.getId(),order.getOrderNumber(), order.getSkuCode(), order.getQuantity(), order.getPrice())).toList();
    }
}
