package com.backend.api.member.controller;

import com.backend.api.member.service.MemberCoinService;
import com.backend.api.member.service.MemberInfoService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원 관련 api")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberCoinController {

    private final MemberService memberService;
    private final MemberCoinService memberCoinService;

    @Operation(summary = "은코인 상승", description = "게임에서 성공하면 은코인이 상승하는 메소드입니다. 현재 코인 증가값은 1입니다.")
    @PostMapping("/plusSilver")
    public ResponseEntity<String> plusSilverCoin(@MemberInfo MemberInfoDto memberInfoDto){
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();

        int coins = 1;
        memberCoinService.plusSilverCoin(memberId, coins);
        return ResponseEntity.ok(String.format("은코인이 %d개 증가하였습니다.", coins));

    }

    @Operation(summary = "은코인 감소", description = "그림을 NFT화하면 은코인을 감소하는 메소드입니다. 현재 코인 감소값은 5입니다.")
    @PostMapping("/minusSilver")
    public ResponseEntity<String> minusSilverCoin(@MemberInfo MemberInfoDto memberInfoDto) {
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();

        int currentSilverCoin = memberCoinService.getSilverCoin(memberId);
        int coinsToDeduct = 5;

        if (currentSilverCoin >= coinsToDeduct) {
            memberCoinService.minusSilverCoin(memberId, coinsToDeduct);
            return ResponseEntity.ok(String.format("은코인이 %d개 감소하였습니다.", coinsToDeduct));
        }
        else{
            return ResponseEntity.ok("은코인이 부족합니다");
        }
    }


    @Operation(summary = "금코인 상승", description = "NFT를 통해 금코인이 상승하는 메소드입니다. 현재 코인 증가값은 1입니다.")
    @PostMapping("/plusGold")
    public ResponseEntity<String> plusGoldCoin(@MemberInfo MemberInfoDto memberInfoDto){
        Member findMember = memberService.findMemberByEmail(memberInfoDto.getEmail());
        Long memberId = findMember.getMemberId();

        int coins = 1;
        memberCoinService.plusGoldCoin(memberId, coins);
        return ResponseEntity.ok(String.format("금코인이 %d개 증가하였습니다.", coins));

    }

}
