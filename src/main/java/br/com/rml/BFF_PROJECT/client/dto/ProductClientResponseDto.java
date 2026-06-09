package br.com.rml.BFF_PROJECT.client.dto;

import java.math.BigDecimal;

// DTO que representa a resposta do SRV — espelha o ProductResponseDto do rml-template-srv
public record ProductClientResponseDto(Long id, String name, BigDecimal price, boolean active) {
}
