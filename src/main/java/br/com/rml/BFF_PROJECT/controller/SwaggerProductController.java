package br.com.rml.BFF_PROJECT.controller;

import br.com.rml.BFF_PROJECT.dto.request.ProductRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Interface com objetivo de separar as annotations do Swagger do controller.
 * TODO: Ajustar descrições de acordo com o domínio do BFF.
 */
@Tag(name = "ProductController")
public interface SwaggerProductController {

    @Operation(summary = "Lista todos os produtos", description = "Retorna a lista completa de produtos do SRV.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok")})
    ResponseEntity<List<ProductResponseDto>> findAll();

    @Operation(summary = "Busca produto por ID", description = "Retorna um produto pelo seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<ProductResponseDto> findById(Long id);

    @Operation(summary = "Cria um produto", description = "Cria um novo produto no SRV.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created")})
        ResponseEntity<ProductResponseDto> create(ProductRequestDto request);

    @Operation(summary = "Atualiza um produto", description = "Atualiza os dados de um produto existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    ResponseEntity<ProductResponseDto> update(Long id, ProductRequestDto request);

    @Operation(summary = "Remove um produto", description = "Remove um produto pelo seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
        ResponseEntity<Void> delete(Long id);
}
