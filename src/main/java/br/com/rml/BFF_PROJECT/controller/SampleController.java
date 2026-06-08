package br.com.rml.BFF_PROJECT.controller;

import br.com.rml.BFF_PROJECT.dto.request.SampleRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.SampleResponseDto;
import br.com.rml.BFF_PROJECT.service.SampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping
    public ResponseEntity<List<SampleResponseDto>> findAll() {
        return ResponseEntity.ok(sampleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SampleResponseDto> create(@Valid @RequestBody SampleRequestDto request) {
        return ResponseEntity.ok(sampleService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SampleResponseDto> update(@PathVariable Long id, @Valid @RequestBody SampleRequestDto request) {
        return ResponseEntity.ok(sampleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
