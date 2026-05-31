package com.skbhati.microservices.order;

import com.skbhati.microservices.order.dto.OrderResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mySQLContainer.start();
    }

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                    "skuCode": "SKU-ABC-123",
                    "quantity": 1,
                    "price": 999.99
                }
                """;

        OrderResponse response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderResponse.class);

        assertThat(response.id(), notNullValue());
        assertThat(response.orderNumber(), notNullValue());

    }

}
