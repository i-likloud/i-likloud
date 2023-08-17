package com.backend.api.store.dto;

import com.backend.api.mypage.dto.ProfileDto;
import com.backend.domain.store.dto.StoreWithAccessoryDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private ProfileDto profile;
    private List<StoreWithAccessoryDto> allAccessoriesWithOwnership;

}