package com.skbhati199.microservices.product.repository;

import com.skbhati199.microservices.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
