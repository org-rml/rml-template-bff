package br.com.rml.BFF_PROJECT.client;

import br.com.rml.BFF_PROJECT.client.dto.SampleClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: substituir "DOMAIN" e "DOMAIN-PROJECT" pelo SRV real que este BFF consome
@FeignClient(name = "rml-srv-DOMAIN-PROJECT", url = "${clients.srv-DOMAIN.url}")
public interface SampleClient {

    @GetMapping("/api/samples")
    List<SampleClientResponseDto> findAll();

    @GetMapping("/api/samples/{id}")
    SampleClientResponseDto findById(@PathVariable Long id);

    @PostMapping("/api/samples")
    SampleClientResponseDto create(@RequestBody Object request);

    @PutMapping("/api/samples/{id}")
    SampleClientResponseDto update(@PathVariable Long id, @RequestBody Object request);

    @DeleteMapping("/api/samples/{id}")
    void delete(@PathVariable Long id);
}
