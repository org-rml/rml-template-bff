package br.com.rml.BFF_PROJECT.service;

import br.com.rml.BFF_PROJECT.client.ProductClient;
import br.com.rml.BFF_PROJECT.dto.request.ProductRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import br.com.rml.BFF_PROJECT.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductClient productClient;

	public List<ProductResponseDto> findAll() {
		return ProductMapper.INSTANCE.toResponse(productClient.findAll());
	}

	public ProductResponseDto findById(Long id) {
		return ProductMapper.INSTANCE.toResponse(productClient.findById(id));
	}

	public ProductResponseDto create(ProductRequestDto request) {
		return ProductMapper.INSTANCE.toResponse(productClient.create(request));
	}

	public ProductResponseDto update(Long id, ProductRequestDto request) {
		return ProductMapper.INSTANCE.toResponse(productClient.update(id, request));
	}

	public void delete(Long id) {
		productClient.delete(id);
	}
}