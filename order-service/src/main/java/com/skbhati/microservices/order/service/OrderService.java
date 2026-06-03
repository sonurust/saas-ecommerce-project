package com.skbhati.microservices.order.service;

import com.skbhati.microservices.order.client.InventoryClient;
import com.skbhati.microservices.order.dto.OrderRequest;
import com.skbhati.microservices.order.dto.OrderResponse;
import com.skbhati.microservices.order.entity.Order;
import com.skbhati.microservices.order.event.OrderPlacedEvent;
import com.skbhati.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderResponse getOrder(Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(order -> new OrderResponse(order.getId(), order.getOrderNumber(), order.getSkuCode(), order.getQuantity(), order.getPrice()))
                .orElse(null);
    }

    public OrderResponse placeOrder(OrderRequest order) {
        var isProductInStock = inventoryClient.isInStock(order.skuCode(), order.quantity());
        if (isProductInStock) {

            Order orderEntity = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(order.price())
                    .quantity(order.quantity())
                    .skuCode(order.skuCode())
                    .build();

            Order newOrder = orderRepository.save(orderEntity);
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.orderNumber(), order.userDetails().email());
            log.info("Start: Order placed event {}", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End: OrderPlacedEvent sent to kafka");

            return new OrderResponse(
                    newOrder.getId(),
                    newOrder.getOrderNumber(),
                    newOrder.getSkuCode(),
                    newOrder.getQuantity(),
                    newOrder.getPrice()
                    );

        } else {
            throw new RuntimeException("Product not found");
        }

    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(order -> new OrderResponse(order.getId(), order.getOrderNumber(), order.getSkuCode(), order.getQuantity(), order.getPrice())).toList();
    }
}
