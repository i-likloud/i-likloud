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
    private String accessoryName;

    @Column
    private int accessoryPrice;

    // 악세사리와 OnetoMany 관계
    @OneToMany(mappedBy = "store")
    private List<Accessory> accessories = new ArrayList<>();

    @Builder
    public Store (Long storeId, String accessoryName, int accessoryPrice){
        this.storeId = storeId;
        this.accessoryName = accessoryName;
        this.accessoryPrice = accessoryPrice;
    }
}
