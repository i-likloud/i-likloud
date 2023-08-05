package com.backend.api.mypage.service;

import com.backend.api.mypage.dto.ProfileDto;
import com.backend.api.nft.dto.NftListResponseDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.nft.entity.Nft;
import com.backend.domain.nft.entity.NftTransfer;
import com.backend.domain.nft.repository.NftRepository;
import com.backend.domain.nft.repository.NftTransferRepository;
import com.backend.domain.nft.service.NftService;
import com.backend.domain.nft.service.NftTransferService;
import com.backend.external.nft.client.NftTokenClient;
import com.backend.external.nft.dto.DeleteTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class MypageService {

    private final MemberService memberService;
    private final NftService nftService;
    private final NftTransferRepository nftTransferRepository;
    private final NftTransferService nftTransferService;
    private final NftTokenClient nftTokenClient;
    private final NftRepository nftRepository;

    @Value("${KAS.client.authorization}")
    private String authorization;

    @Value("${KAS.client.contract-alias}")
    private String contract;

    @Transactional(readOnly = true)
    public ProfileDto getMyInfo(String email) {
        Member member = memberService.findMemberByEmail(email);
        return ProfileDto.of(member);
    }

    @Transactional(readOnly = true)
    public ProfileDto getMyProfile(String email) {
        Member member = memberService.findMemberByEmail(email);
        return ProfileDto.of(member);
    }

    @Transactional
    public ProfileDto editNickname(Member member, String nickname){
        member.editNickname(nickname);
        return ProfileDto.of(member);
    }

    @Transactional
    public ProfileDto editProfile(String email, ProfileDto.editRequest request){
        Member member = memberService.findMemberByEmail(email);
        member.editProfile(request.getProfileFace(), request.getProfileColor(), request.getProfileAccessory());
        return ProfileDto.of(member);

    }

    // 선물 수락
    @Transactional
    @CacheEvict(value = "nft", key = "#member.memberId")
    public NftListResponseDto acceptGift(Long transferId, Long nftId, Member member) {
        Nft nft = nftService.findNftById(nftId);
        // 소유자 변경
        nft.setMember(member);
        // 거래 삭제
        nftTransferRepository.deleteById(transferId);

        return new NftListResponseDto(nft);
    }

    // 선물 거절
    @Transactional
    public void rejectGift(Long transferId, Long nftId, Member member) {
        NftTransfer nftTransfer = nftTransferService.findTransferById(transferId);

        DeleteTokenDto.Request request = DeleteTokenDto.Request.builder()
                        .from(member.getWallet())
                        .build();

        // 토큰 소멸
        nftTokenClient.deleteToken("1001", authorization, contract, nftTransfer.getTokenId(), request);
        // 거래 삭제
        nftTransferRepository.deleteById(transferId);
        // NFT 삭제
        nftRepository.deleteById(nftId);

    }
}

