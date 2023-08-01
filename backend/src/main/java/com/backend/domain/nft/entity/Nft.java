package com.backend.domain.nft.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.entity.Member;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nft extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nftId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "nft")
    private Drawing drawing;

    @Column(nullable = false)
    private String nftImageUrl;

    @Column(nullable = false)
    private String contract;

    @Builder
    public Nft(String nftImageUrl, String contract) {
        this.nftImageUrl = nftImageUrl;
        this.contract = contract;
    }


}
