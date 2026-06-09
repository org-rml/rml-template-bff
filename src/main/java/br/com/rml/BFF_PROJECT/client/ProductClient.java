package br.com.rml.BFF_PROJECT.client;

import br.com.rml.BFF_PROJECT.client.dto.ProductClientResponseDto;
import br.com.rml.BFF_PROJECT.dto.request.ProductRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "rml-srv-product", url = "${clients.srv-product.url}")
public interface ProductClient {

	@GetMapping("/api/products")
	List<ProductClientResponseDto> findAll();

	@GetMapping("/api/products/{id}")
	ProductClientResponseDto findById(@PathVariable Long id);

	@PostMapping("/api/products")
	ProductClientResponseDto create(@RequestBody ProductRequestDto request);

	@PutMapping("/api/products/{id}")
	ProductClientResponseDto update(@PathVariable Long id, @RequestBody ProductRequestDto request);

	@DeleteMapping("/api/products/{id}")
	void delete(@PathVariable Long id);
}
