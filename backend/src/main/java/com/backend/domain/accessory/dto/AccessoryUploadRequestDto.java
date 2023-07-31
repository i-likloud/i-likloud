package com.backend.domain.accessory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessoryUploadRequestDto {
    private String itemName;
    private int itemPrice;
}
