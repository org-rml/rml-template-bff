package br.com.rml.BFF_PROJECT.dto.response;

import com.rml.common.dto.base.BaseLongDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleResponseDto extends BaseLongDTO {

    private String name;
}
