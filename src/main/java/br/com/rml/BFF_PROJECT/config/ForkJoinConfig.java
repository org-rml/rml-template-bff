package br.com.rml.BFF_PROJECT.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

/**
 * Configura o pool de threads para suportar chamadas paralelas a múltiplos SRVs
 * usando CompletableFuture ou outras abstrações de concorrência.
 *
 * O paralelismo é controlado pela propriedade "server.parallelism" (default: 200).
 * Ajuste conforme o número de SRVs consumidos e a carga esperada no BFF.
 *
 * Exemplo de uso:
 * <pre>
 *   var produtos = CompletableFuture.supplyAsync(() -> produtoClient.findAll(), forkJoinPool);
 *   var estoque  = CompletableFuture.supplyAsync(() -> estoqueClient.findAll(), forkJoinPool);
 *   CompletableFuture.allOf(produtos, estoque).join();
 * </pre>
 *
 * @see java.util.concurrent.ForkJoinPool
 * @see java.util.concurrent.CompletableFuture
 */
@Configuration
public class ForkJoinConfig {

    @Value("${server.parallelism}")
    private int parallelism;

    @Bean
    public ForkJoinPool forkJoinPool() {
        return new ForkJoinPool(parallelism);
    }

    @PostConstruct
    public void setProperties() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(parallelism));
    }
}
