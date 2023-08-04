package com.backend.domain.nft.entity;

import com.backend.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class NftTransfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @Column(nullable = false)
    private Long toMember;

    @Column(nullable = false)
    private Long fromMember;

    @Column(nullable = false)
    private String tokenId;

    private String transactionHash;

    private String message;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nft_id")
    private Nft nft;


}
