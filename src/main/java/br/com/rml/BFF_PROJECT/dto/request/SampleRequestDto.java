package br.com.rml.BFF_PROJECT.dto.request;

import br.com.rml.common.dto.base.BaseLongDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleRequestDto extends BaseLongDTO {

    @NotBlank
    private String name;
}
