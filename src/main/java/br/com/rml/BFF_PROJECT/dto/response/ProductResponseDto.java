package br.com.rml.BFF_PROJECT.dto.response;

import java.math.BigDecimal;

public record ProductResponseDto(Long id, String name, BigDecimal price, boolean active) {
}
