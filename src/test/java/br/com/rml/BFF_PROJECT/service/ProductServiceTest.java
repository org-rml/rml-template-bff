package br.com.rml.BFF_PROJECT.service;

import br.com.rml.BFF_PROJECT.client.ProductClient;
import br.com.rml.BFF_PROJECT.client.dto.ProductClientResponseDto;
import br.com.rml.BFF_PROJECT.dto.request.ProductRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Teste unitário do ProductService.
 *
 * Testa apenas a orquestração: o service chama o client correto e
 * o mapper converte o resultado. O ProductClient é mockado — o service
 * não sabe se está chamando um SRV real ou um mock.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductClient productClient;

    @InjectMocks
    ProductService productService;

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Test
    void findAll_deveRetornarListaMapeada_quandoClientRetornaDados() {
        var clientResponse = new ProductClientResponseDto(1L, "Notebook", new BigDecimal("4500.00"), true);
        when(productClient.findAll()).thenReturn(List.of(clientResponse));

        List<ProductResponseDto> result = productService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).name()).isEqualTo("Notebook");
        assertThat(result.get(0).price()).isEqualByComparingTo("4500.00");
        verify(productClient).findAll();
    }

    @Test
    void findAll_deveRetornarListaVazia_quandoClientNaoRetornaDados() {
        when(productClient.findAll()).thenReturn(List.of());

        List<ProductResponseDto> result = productService.findAll();

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    void findById_deveRetornarProdutoMapeado_quandoExiste() {
        var clientResponse = new ProductClientResponseDto(1L, "Monitor", new BigDecimal("1200.00"), true);
        when(productClient.findById(1L)).thenReturn(clientResponse);

        ProductResponseDto result = productService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Monitor");
        verify(productClient).findById(1L);
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    void create_deveDelegarAoClientEMapearResposta() {
        var request = new ProductRequestDto("Teclado", new BigDecimal("300.00"), true);
        var clientResponse = new ProductClientResponseDto(2L, "Teclado", new BigDecimal("300.00"), true);
        when(productClient.create(request)).thenReturn(clientResponse);

        ProductResponseDto result = productService.create(request);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("Teclado");
        verify(productClient).create(request);
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    void update_deveDelegarAoClientComIdCorreto() {
        var request = new ProductRequestDto("Mouse Pro", new BigDecimal("250.00"), true);
        var clientResponse = new ProductClientResponseDto(3L, "Mouse Pro", new BigDecimal("250.00"), true);
        when(productClient.update(3L, request)).thenReturn(clientResponse);

        ProductResponseDto result = productService.update(3L, request);

        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.name()).isEqualTo("Mouse Pro");
        verify(productClient).update(3L, request);
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    void delete_deveDelegarAoClient() {
        productService.delete(1L);

        verify(productClient).delete(1L);
    }

    @Test
    void delete_naoDeveChamarOutrosMetodos_aoChamarDelete() {
        productService.delete(5L);

        verify(productClient).delete(5L);
        verifyNoMoreInteractions(productClient);
    }
}
