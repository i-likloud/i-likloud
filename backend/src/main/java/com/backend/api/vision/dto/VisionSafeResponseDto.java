package com.backend.api.vision.dto;

import com.google.cloud.vision.v1.Likelihood;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisionSafeResponseDto {

    private Likelihood racy;
    private Likelihood medical;
    private Likelihood spoofed;
    private Likelihood adult;
    private Likelihood violence;

}
