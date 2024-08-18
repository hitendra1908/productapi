package com.api.productapi.exception;

public non-sealed class ProductRequestException extends ProductsException {
    public ProductRequestException(final String message) {
        super(message);
    }
}
