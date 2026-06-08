package br.com.rml.BFF_PROJECT.mapper;

import br.com.rml.BFF_PROJECT.client.dto.SampleClientResponseDto;
import br.com.rml.BFF_PROJECT.dto.response.SampleResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SampleMapper {

    SampleResponseDto toResponse(SampleClientResponseDto clientResponse);

    List<SampleResponseDto> toResponseList(List<SampleClientResponseDto> clientResponses);
}
