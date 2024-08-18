package com.api.productapi.service;

import com.api.productapi.dto.ProductRequestDto;
import com.api.productapi.exception.ProductNotFoundException;
import com.api.productapi.exception.ProductRequestException;
import com.api.productapi.model.Product;
import com.api.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(final ProductRequestDto productRequestDto) {
        final Product validProduct = getValidatProduct(productRequestDto);
        return productRepository.save(validProduct);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: "+id));
    }

    public Product updateProduct(UUID id, ProductRequestDto productRequest) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: "+id));

        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: "+id));
        productRepository.delete(product);
    }

    private Product getValidatProduct(ProductRequestDto productRequestDto) {
        final String name = productRequestDto.name();
        if(name.isBlank() || name.length() < 2) {
            throw new ProductRequestException("Invalid product name: name must be at least 2 characters");
        }
        if(productRequestDto.price() == null) {
            throw new ProductRequestException("Invalid product request: price can't be null or empty");
        }
        return mapToProduct(productRequestDto);
    }

    private Product mapToProduct(final ProductRequestDto productRequestDto) {
        return Product.builder()
                .name(productRequestDto.name())
                .description(productRequestDto.description())
                .price(productRequestDto.price())
                .build();
    }
}
