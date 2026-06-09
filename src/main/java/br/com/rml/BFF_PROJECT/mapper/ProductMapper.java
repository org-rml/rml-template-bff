package br.com.rml.BFF_PROJECT.mapper;

import br.com.rml.BFF_PROJECT.client.dto.ProductClientResponseDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	ProductResponseDto toResponse(ProductClientResponseDto clientResponse);

	List<ProductResponseDto> toResponse(List<ProductClientResponseDto> clientResponses);
}