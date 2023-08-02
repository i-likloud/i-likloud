package com.backend.domain.nft.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.entity.Member;
import lombok.*;

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

    @Column(nullable = false)
    private String nftImageUrl;

    @Column(nullable = false)
    private String contract;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String nftMetadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    @Builder
    public Nft(String nftImageUrl, String contract) {
        this.nftImageUrl = nftImageUrl;
        this.contract = contract;
    }

}
