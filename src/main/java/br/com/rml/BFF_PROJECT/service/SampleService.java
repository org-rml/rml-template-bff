package br.com.rml.BFF_PROJECT.service;

import br.com.rml.BFF_PROJECT.client.SampleClient;
import br.com.rml.BFF_PROJECT.dto.request.SampleRequestDto;
import br.com.rml.BFF_PROJECT.dto.response.SampleResponseDto;
import br.com.rml.BFF_PROJECT.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleClient sampleClient;
    private final SampleMapper sampleMapper;

    public List<SampleResponseDto> findAll() {
        return sampleMapper.toResponseList(sampleClient.findAll());
    }

    public SampleResponseDto findById(Long id) {
        return sampleMapper.toResponse(sampleClient.findById(id));
    }

    public SampleResponseDto create(SampleRequestDto request) {
        return sampleMapper.toResponse(sampleClient.create(request));
    }

    public SampleResponseDto update(Long id, SampleRequestDto request) {
        return sampleMapper.toResponse(sampleClient.update(id, request));
    }

    public void delete(Long id) {
        sampleClient.delete(id);
    }
}
