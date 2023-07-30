package com.backend.domain.store.dto;

import com.backend.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class StoreWithAccessoryDto {

    private Long storeId;
    private String itemName;
    private int itemPrice;
    private boolean owned;

    public StoreWithAccessoryDto(Store store) {
        this.storeId = store.getStoreId();
        this.itemName = store.getItemName();
        this.itemPrice = store.getItemPrice();
        this.owned = store.isOwned();
    }

}
