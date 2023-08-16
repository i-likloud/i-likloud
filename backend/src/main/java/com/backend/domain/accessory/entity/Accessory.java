package com.backend.domain.accessory.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.member.entity.Member;
import com.backend.domain.store.entity.Store;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Accessory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    // repository 테스트용
    @Override
    public String toString() {
        return "Accessory [id=" + accessoryId + ", member=" + member + ", store=" + store + "]";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accessory accessory = (Accessory) o;
        return Objects.equals(accessoryId, accessory.accessoryId) &&
                Objects.equals(member, accessory.member) &&
                Objects.equals(store, accessory.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessoryId, member, store);
    }

}
