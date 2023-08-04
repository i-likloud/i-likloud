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

    private String status;

    private String toMember;

    private String fromMember;

    private String keyId;

    private String tokenId;

    private String transactionHash;

    private String message;


}
