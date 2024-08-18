package com.api.productapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ProductsException.class)
    public ProblemDetail handleFileException(ProductsException exception) {
        return switch (exception) {
            case ProductNotFoundException e -> createProblemDetail(e, HttpStatus.BAD_REQUEST, "Product not found");
            case ProductRequestException e -> createProblemDetail(e, HttpStatus.BAD_REQUEST, "Invalid product request");
            default -> createProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Exception while processing file");
        };
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("Resource not found: ", exception);
        return createProblemDetail(exception, HttpStatus.BAD_REQUEST, "Resource you are trying to access not found!");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Bad Request ", exception);
        return createProblemDetail(exception, HttpStatus.BAD_REQUEST, "Invalid request");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllOtherExceptions(Exception exception) {
        log.error("Generic exception occurred while processing the request: ", exception);
        return createProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
    }

    private static ProblemDetail createProblemDetail(Exception exception, HttpStatus status, String title) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
