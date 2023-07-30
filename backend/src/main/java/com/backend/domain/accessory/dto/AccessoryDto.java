package com.backend.domain.accessory.dto;

import com.backend.domain.accessory.entity.Accessory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AccessoryDto {

    private Long accessoryId;
    private String accessoryName;
    private int accessoryPrice;

    public AccessoryDto(Accessory accessory){
        this.accessoryId = accessory.getAccessoryId();
        this.accessoryName = accessory.getStore().getItemName();
        this.accessoryPrice = accessory.getStore().getItemPrice();

    }
}
