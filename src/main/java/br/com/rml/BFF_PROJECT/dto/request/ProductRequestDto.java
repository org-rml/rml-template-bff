package br.com.rml.BFF_PROJECT.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDto(@NotBlank(message = "name é obrigatório") String name,
		@NotNull(message = "price é obrigatório") @DecimalMin(value = "0.0", message = "price deve ser >= 0") BigDecimal price,
		boolean active) {
}
