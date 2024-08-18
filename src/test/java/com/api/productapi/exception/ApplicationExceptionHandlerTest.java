package com.api.productapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApplicationExceptionHandler();
    }

    @Test
    void handleProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException("Product not found");

        ProblemDetail problemDetail = handler.handleFileException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Product not found", problemDetail.getTitle());
        assertEquals("Product not found", problemDetail.getDetail());
    }

    @Test
    void handleProductRequestException() {
        ProductRequestException exception = new ProductRequestException("Invalid product request");

        ProblemDetail problemDetail = handler.handleFileException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Invalid product request", problemDetail.getTitle());
        assertEquals("Invalid product request", problemDetail.getDetail());
    }

    @Test
    void handleGenericProductsException() {
        ProductsException exception = new ProductsException("Generic product exception");

        ProblemDetail problemDetail = handler.handleFileException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Exception while processing file", problemDetail.getTitle());
        assertEquals("Generic product exception", problemDetail.getDetail());
    }

    @Test
    void handleNoResourceFoundException() {
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, "/test");

        ProblemDetail problemDetail = handler.handleNoResourceFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Resource you are trying to access not found!", problemDetail.getTitle());
        assertEquals("No static resource /test.", problemDetail.getDetail());
    }

    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid request");

        ProblemDetail problemDetail = handler.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Invalid request", problemDetail.getTitle());
        assertEquals("Invalid request", problemDetail.getDetail());
    }

    @Test
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Product name already exists");

        ProblemDetail problemDetail = handler.handleDataIntegrityViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Invalid request: product name already exist", problemDetail.getTitle());
        assertEquals("Product name already exists", problemDetail.getDetail());
    }

    @Test
    void handleGenericException() {
        Exception exception = new Exception("Generic exception occurred");

        ProblemDetail problemDetail = handler.handleAllOtherExceptions(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Something went wrong!", problemDetail.getTitle());
        assertEquals("Generic exception occurred", problemDetail.getDetail());
    }
}
