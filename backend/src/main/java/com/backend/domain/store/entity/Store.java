package com.backend.domain.store.entity;

import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column
    private String itemName;

    @Column
    private int itemPrice;

    private boolean owned;

    // 악세사리와 OnetoMany 관계
    @OneToMany(mappedBy = "store")
    private List<Accessory> accessories = new ArrayList<>();

    @Builder
    public Store (Long storeId, String itemName, int itemPrice, boolean owned){
        this.storeId = storeId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.owned = owned;
    }
}
