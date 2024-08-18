package com.api.productapi.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto (String name, String description, BigDecimal price) {

}
