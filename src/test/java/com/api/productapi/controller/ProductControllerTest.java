package com.api.productapi.controller;

import com.api.productapi.dto.ProductRequestDto;
import com.api.productapi.model.Product;
import com.api.productapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private ProductRequestDto productRequestDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setDescription("Test Description");

        productRequestDto = ProductRequestDto.builder()
                        .name("Test Product")
                        .description("Test Description")
                        .build();
    }

    @Test
    void testCreateProduct() {
        when(productService.createProduct(any(ProductRequestDto.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(productRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).createProduct(any(ProductRequestDto.class));
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Collections.singletonList(product);
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById_ProductFound() {
        when(productService.getProductById(any(UUID.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(UUID.randomUUID());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).getProductById(any(UUID.class));
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productService.getProductById(any(UUID.class))).thenReturn(null);

        ResponseEntity<Product> response = productController.getProductById(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).getProductById(any(UUID.class));
    }

    @Test
    void testUpdateProduct() {
        when(productService.updateProduct(any(UUID.class), any(ProductRequestDto.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(UUID.randomUUID(), productRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).updateProduct(any(UUID.class), any(ProductRequestDto.class));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(any(UUID.class));

        ResponseEntity<Void> response = productController.deleteProduct(UUID.randomUUID());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(any(UUID.class));
    }
}
