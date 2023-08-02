package com.backend.api.nft.controller;

import com.backend.api.member.service.MemberCoinService;
import com.backend.api.nft.dto.NftResponseDto;
import com.backend.api.nft.service.NftService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.nft.entity.Nft;
import com.backend.external.nft.dto.TokenListDto;
import com.backend.external.nft.dto.WalletDto;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "NFT" , description = "NFT 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nft")
public class NftTokenController {

    private final NftService nftService;
    private final MemberService memberService;
    private final MemberCoinService memberCoinService;

    // 지갑 생성
    @PostMapping("/wallet")
    @Operation(summary = "NFT 지갑 발급", description = "NFT생성하고 보관하기 위한 지갑 생성 메서드입니다.")
    public WalletDto.Response createWallet(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        // 지갑생성하고 멤버필드 업데이트
        return nftService.createWallet(member);
    }

    // 토큰 발행
    @PostMapping("/token/{drawingId}")
    @Operation(summary = "NFT 토큰 발행", description = "자신의 그림으로 토큰을 발행합니다.")
    public ResponseEntity<NftResponseDto> createToken(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto){
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        if(member.getSilverCoin() < 1){
            throw new BusinessException(ErrorCode.NOT_ENOUGH_COIN);
        }

        Nft nft = nftService.uploadNftAndCreateToken(drawingId, member);
        NftResponseDto nftResponseDto = new NftResponseDto(nft);
        // 은코인 감소, 금코인 증가
        memberCoinService.minusSilverCoin(member, 1);
        memberCoinService.plusGoldCoin(member, 1);
        return ResponseEntity.ok().body(nftResponseDto);
    }

    // 특정 사용자 지갑 조회
    @GetMapping("/wallet/{memberId}")
    @Operation(summary = "특정 멤버 NFT 지갑 조회", description = "특정 사용자의 NFT 발행 내역을 조회합니다.")
    public TokenListDto.Response getTokenList(@PathVariable Long memberId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        return nftService.getTokenList(member);
    }

    // 모든 토큰 조회
    @GetMapping("/token")
    @Operation(summary = "발행된 모든 토큰 조회", description = "모든 이용자가 발행한 토큰들을 조회합니다.")
    public TokenListDto.Response getAllTokenList(){
        return nftService.getAllTokenList();
    }

}
