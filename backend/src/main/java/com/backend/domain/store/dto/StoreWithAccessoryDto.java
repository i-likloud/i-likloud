package com.backend.domain.store.dto;

import com.backend.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class StoreWithAccessoryDto {

    private Long storeId;
    private String accessoryName;
    private int accessoryPrice;
    private boolean owned;

    public StoreWithAccessoryDto(Store store, boolean owned) {
        this.storeId = store.getStoreId();
        this.accessoryName = store.getAccessoryName();
        this.accessoryPrice = store.getAccessoryPrice();
        this.owned = owned;
    }
}
