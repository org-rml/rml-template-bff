package br.com.rml.BFF_PROJECT.mapper;

import br.com.rml.BFF_PROJECT.client.dto.ProductClientResponseDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste unitário do ProductMapper.
 * Sem Spring — usa INSTANCE diretamente.
 * Garante que o MapStruct mapeou todos os campos corretamente.
 */
class ProductMapperTest {

    private final ProductMapper mapper = ProductMapper.INSTANCE;

    @Test
    void toResponse_deveMappearTodosOsCampos() {
        var client = new ProductClientResponseDto(1L, "Notebook", new BigDecimal("4500.00"), true);

        ProductResponseDto result = mapper.toResponse(client);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Notebook");
        assertThat(result.price()).isEqualByComparingTo("4500.00");
        assertThat(result.active()).isTrue();
    }

    @Test
    void toResponse_deveRetornarNull_quandoInputNull() {
        ProductResponseDto result = mapper.toResponse((ProductClientResponseDto) null);

        assertThat(result).isNull();
    }

    @Test
    void toResponse_lista_deveMappearTodosOsItens() {
        var c1 = new ProductClientResponseDto(1L, "Mouse", new BigDecimal("150.00"), true);
        var c2 = new ProductClientResponseDto(2L, "Teclado", new BigDecimal("300.00"), false);

        List<ProductResponseDto> result = mapper.toResponse(List.of(c1, c2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Mouse");
        assertThat(result.get(1).name()).isEqualTo("Teclado");
        assertThat(result.get(1).active()).isFalse();
    }

    @Test
    void toResponse_lista_deveRetornarNull_quandoInputNull() {
        List<ProductResponseDto> result = mapper.toResponse((List<ProductClientResponseDto>) null);

        assertThat(result).isNull();
    }
}
