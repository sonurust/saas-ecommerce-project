package com.skbhati.microservices.order.config;

import com.skbhati.microservices.order.client.InventoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class RestClientConfig {


    @Value("${inventory.url}")
    String inventoryUrl;

    @Bean
    public InventoryClient inventoryClient() {
        log.info("Creating new InventoryClient instance " + inventoryUrl);
        RestClient restClient =  RestClient.builder()
//                .baseUrl("http://localhost:8082")
                .baseUrl(inventoryUrl)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }
}
