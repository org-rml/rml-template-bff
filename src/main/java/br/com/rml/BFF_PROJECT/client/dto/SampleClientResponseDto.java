package br.com.rml.BFF_PROJECT.client.dto;

import com.rml.common.dto.base.BaseLongDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

// DTO que representa a resposta do SRV (pode ser diferente do DTO do BFF)
@Data
@EqualsAndHashCode(callSuper = true)
public class SampleClientResponseDto extends BaseLongDTO {

    private String name;
}
