package com.api.productapi;

import com.api.productapi.dto.ProductRequestDto;
import com.api.productapi.model.Product;
import com.api.productapi.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class ProductsIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ProductRepository productRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private UUID id;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/api/products";

        productRepository.deleteAll();

        final Product product1 = Product.builder()
                .name("Product 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(11.0))
                .build();

        final Product savedProduct = productRepository.save(product1);

        id = savedProduct.getId();
    }

    @Test
    void testCreateProduct() {
        final ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.0))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(productRequestDto)
        .when()
                .post()
        .then()
                .statusCode(201)
                .body(containsString("Test Product"), containsString("Test Description"));
    }

    @Test
    void testUpdateProduct() {
        final ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.0))
                .build();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(productRequestDto)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .body(containsString("Test Product"), containsString("Test Description"));
    }

    @Test
    void testFindAll() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(
                        ".", hasSize(1),
                        "[0].id", equalTo(id.toString()),
                        "[0].name", equalTo("Product 1"),
                        "[0].description", equalTo("Description 1")
                );

    }

    @Test
    void testFindById() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(
                        "id", equalTo(id.toString()),
                        "name", equalTo("Product 1"),
                        "description", equalTo("Description 1")
                );
    }

    @Test
    public void testDeleteById() {
        given()
                .pathParam("id", id)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }
}
