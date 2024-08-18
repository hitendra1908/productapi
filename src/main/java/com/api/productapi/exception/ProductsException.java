package com.api.productapi.exception;

public sealed class ProductsException extends RuntimeException permits ProductNotFoundException,
        ProductRequestException {
    public ProductsException(String message) {
        super(message);
    }
}
