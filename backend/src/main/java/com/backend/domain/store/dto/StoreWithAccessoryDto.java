package com.backend.domain.store.dto;

import com.backend.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreWithAccessoryDto {

    private Long storeId;
    private String itemName;
    private int itemPrice;

    public StoreWithAccessoryDto(Store store) {
        this.storeId = store.getStoreId();
        this.itemName = store.getItemName();
        this.itemPrice = store.getItemPrice();
    }

}
