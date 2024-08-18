package com.api.productapi.exception;

public non-sealed class ProductNotFoundException extends ProductsException {
    public ProductNotFoundException(final String message) {
        super(message);
    }
}
