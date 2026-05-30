package com.skbhati199.microservices.product.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@Data
public class Product {
    @Id
    String id;
    String name;
    String description;

    BigDecimal price;
}
