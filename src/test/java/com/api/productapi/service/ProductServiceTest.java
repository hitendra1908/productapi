package com.api.productapi.service;

import com.api.productapi.dto.ProductRequestDto;
import com.api.productapi.exception.ProductNotFoundException;
import com.api.productapi.exception.ProductRequestException;
import com.api.productapi.model.Product;
import com.api.productapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequestDto productRequestDto;
    private Product product;
    private BigDecimal price;

    @BeforeEach
    void setUp() {
        price = BigDecimal.valueOf(100.0);
        productRequestDto = new ProductRequestDto("Product Name", "Product Description", price);
        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Product Name")
                .description("Product Description")
                .price(price)
                .build();
    }

    @Test
    void createProduct_validProduct_createsProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(productRequestDto);

        assertNotNull(createdProduct);
        assertEquals(productRequestDto.name(), createdProduct.getName());
        assertEquals(productRequestDto.description(), createdProduct.getDescription());
        assertEquals(productRequestDto.price(), createdProduct.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_returnsProductList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> productList = productService.getAllProducts();

        assertNotNull(productList);
        assertEquals(1, productList.size());
        assertEquals(product.getId(), productList.getFirst().getId());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_existingId_returnsProduct() {
        UUID id = product.getId();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(id);

        assertNotNull(foundProduct);
        assertEquals(id, foundProduct.getId());

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void getProductById_nonExistingId_throwsProductNotFoundException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(id));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Product not found with id:"));

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void updateProduct_existingId_updatesProduct() {
        UUID id = product.getId();
        ProductRequestDto updatedRequest = new ProductRequestDto("Updated Name", "Updated Description", BigDecimal.valueOf(150.0));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(id, updatedRequest);

        assertNotNull(updatedProduct);
        assertEquals(updatedRequest.name(), updatedProduct.getName());
        assertEquals(updatedRequest.description(), updatedProduct.getDescription());
        assertEquals(updatedRequest.price(), updatedProduct.getPrice());

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_nonExistingId_throwsProductNotFoundException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(id, productRequestDto));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Product not found with id:"));

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_existingId_deletesProduct() {
        UUID id = product.getId();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.deleteProduct(id);

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_nonExistingId_throwsProductNotFoundException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(id));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Product not found with id:"));

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void createProduct_invalidName_throwsProductRequestException() {
        ProductRequestDto invalidRequest = new ProductRequestDto("", "Description", price);

        ProductRequestException exception = assertThrows(ProductRequestException.class, () -> productService.createProduct(invalidRequest));

        assertNotNull(exception);
        assertEquals("Invalid product name: name must be at least 2 characters", exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_nullPrice_throwsProductRequestException() {
        ProductRequestDto invalidRequest = new ProductRequestDto("Valid Name", "Description", null);

        ProductRequestException exception = assertThrows(ProductRequestException.class, () -> productService.createProduct(invalidRequest));

        assertNotNull(exception);
        assertEquals("Invalid product request: price can't be null or empty", exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
    }
}
