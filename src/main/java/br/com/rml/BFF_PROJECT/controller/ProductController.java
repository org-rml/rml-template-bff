package br.com.rml.BFF_PROJECT.controller;

import br.com.rml.BFF_PROJECT.dto.request.ProductRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import br.com.rml.BFF_PROJECT.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController implements SwaggerProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> findAll() {
		LOGGER.info("Listando todos os produtos");
		return ResponseEntity.ok(productService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
		LOGGER.info("Buscando produto id: {}", id);
		return ResponseEntity.ok(productService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto request) {
		LOGGER.info("Criando produto: {}", request.name());
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDto> update(@PathVariable Long id,
			@Valid @RequestBody ProductRequestDto request) {
		LOGGER.info("Atualizando produto id: {}", id);
		return ResponseEntity.ok(productService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		LOGGER.info("Removendo produto id: {}", id);
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}