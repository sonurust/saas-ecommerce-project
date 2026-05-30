package com.skbhati199.microservices.product.service;

import com.skbhati199.microservices.product.dto.ProductRequest;
import com.skbhati199.microservices.product.dto.ProductResponse;
import com.skbhati199.microservices.product.entity.Product;
import com.skbhati199.microservices.product.repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product {} created", product);
        return product;
    }


    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(),product.getName(),product.getDescription(), product.getPrice()))
                .toList();
    }


}
