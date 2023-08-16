package com.backend.domain.accessory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "예시 DTO")
public class AccessoryUploadRequestDto {
    @Schema(example = "귀여운 우산")
    private String accessoryName;
    @Schema(example = "5")
    private int accessoryPrice;
}
