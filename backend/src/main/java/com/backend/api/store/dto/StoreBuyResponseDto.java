package com.backend.api.store.dto;


import com.backend.api.mypage.dto.ProfileDto;
import com.backend.domain.accessory.dto.AccessoryDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreBuyResponseDto {

    private ProfileDto profile;
    private List<AccessoryDto> myAccessory;
}
