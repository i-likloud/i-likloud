package com.backend.api.nft.service;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.entity.NftTransfer;
import com.backend.domain.nft.repository.NftTransferRepository;
import com.backend.domain.nft.service.NftService;
import com.backend.external.nft.client.NftTransferClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NftTransferService {

    private final NftService nftService;
    private final MemberService memberService;
    private final NftTransferClient nftTransferClient;
    private final NftTransferRepository nftTransferRepository;

    @Value("${KAS.client.authorization}")
    private String Authorization;

    @Value("${KAS.client.contract}")
    private String contract;

    public void transferToken(Member fromMember, Long nftId, Long toMemberId) {
        // 보낼 토큰
        Nft nft = nftService.findNftById(nftId);
        // 토큰 받을 멤버
        Member toMember = memberService.findMemberById(toMemberId);


        uploadNftTransfer(nft, fromMember, toMember);
        nftTransferClient.transferToken("1001", Authorization, contract, nft.getTokenId());
    }

    @Transactional
    // 거래내역 등록
    public void uploadNftTransfer(Nft nft, Member fromMember, Member toMember){
        NftTransfer nftTransfer = NftTransfer.builder()
                .status("Submitted")
                .toMember(toMember.getWallet())
                .fromMember(fromMember.getWallet())
                .keyId(fromMember.getKeyId())
                .tokenId(nft.getTokenId())
                .build();

        nftTransferRepository.save(nftTransfer);
    }
}
