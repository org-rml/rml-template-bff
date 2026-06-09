package br.com.rml.BFF_PROJECT.controller;

import br.com.rml.BFF_PROJECT.adapter.exception.handler.RestExceptionHandler;
import br.com.rml.BFF_PROJECT.dto.request.ProductRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.ProductResponseDto;
import br.com.rml.BFF_PROJECT.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de slice do ProductController.
 *
 * @WebMvcTest sobe apenas a camada web (sem Spring Security, sem Feign).
 * O ProductService é mockado. Testa: parsing HTTP, validações de campo,
 * status codes e JSON de resposta.
 */
@WebMvcTest(ProductController.class)
@Import(RestExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProductService productService;

    // -------------------------------------------------------------------------
    // POST /api/products
    // -------------------------------------------------------------------------

    @Test
    void create_deveRetornar201_quandoDadosValidos() throws Exception {
        var request = new ProductRequestDto("Notebook", new BigDecimal("4500.00"), true);
        var response = new ProductResponseDto(1L, "Notebook", new BigDecimal("4500.00"), true);

        when(productService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Notebook"))
                .andExpect(jsonPath("$.price").value(4500.00));
    }

    @Test
    void create_deveRetornar400_quandoNameEmBranco() throws Exception {
        String body = """
                { "name": "", "price": 100.00, "active": true }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void create_deveRetornar400_quandoPrecoNulo() throws Exception {
        String body = """
                { "name": "Produto", "active": true }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void create_deveRetornar400_quandoPrecoNegativo() throws Exception {
        String body = """
                { "name": "Produto", "price": -1.00, "active": true }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    // -------------------------------------------------------------------------
    // GET /api/products
    // -------------------------------------------------------------------------

    @Test
    void findAll_deveRetornar200_comListaDeProdutos() throws Exception {
        var response = new ProductResponseDto(1L, "Monitor", new BigDecimal("1200.00"), true);
        when(productService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Monitor"));
    }

    @Test
    void findAll_deveRetornar200_comListaVazia() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // -------------------------------------------------------------------------
    // GET /api/products/{id}
    // -------------------------------------------------------------------------

    @Test
    void findById_deveRetornar200_quandoExiste() throws Exception {
        var response = new ProductResponseDto(1L, "Mouse", new BigDecimal("150.00"), true);
        when(productService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    // -------------------------------------------------------------------------
    // PUT /api/products/{id}
    // -------------------------------------------------------------------------

    @Test
    void update_deveRetornar200_quandoDadosValidos() throws Exception {
        var request = new ProductRequestDto("Notebook Pro", new BigDecimal("5000.00"), true);
        var response = new ProductResponseDto(1L, "Notebook Pro", new BigDecimal("5000.00"), true);

        when(productService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Notebook Pro"));
    }

    @Test
    void update_deveRetornar400_quandoNameEmBranco() throws Exception {
        String body = """
                { "name": "", "price": 100.00, "active": true }
                """;

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    // -------------------------------------------------------------------------
    // DELETE /api/products/{id}
    // -------------------------------------------------------------------------

    @Test
    void delete_deveRetornar204_quandoChamadoComIdValido() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).delete(1L);
    }
}
