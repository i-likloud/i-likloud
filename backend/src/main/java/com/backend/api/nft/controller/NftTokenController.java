package com.backend.api.nft.controller;

import com.backend.api.member.service.MemberCoinService;
import com.backend.api.nft.dto.NftResponseDto;
import com.backend.api.nft.service.NftApiService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.nft.entity.Nft;
import com.backend.external.nft.dto.TokenListDto;
import com.backend.external.nft.dto.WalletDto;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.ErrorResponse;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfo;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import com.backend.global.util.CustomApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "NFT" , description = "NFT 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/nft")
public class NftTokenController {

    private final NftApiService nftApiService;
    private final MemberService memberService;
    private final MemberCoinService memberCoinService;

    // 지갑 생성
    @PostMapping("/wallet")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"),  @ExampleObject( name = "400_NFT-002", value = "이미 발행된 토큰입니다."), @ExampleObject( name = "400_NFT-005", value = "이미 지갑이 있습니다. 사용자당 지갑은 1개 입니다."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
    @Operation(summary = "NFT 지갑 발급", description = "지갑을 생성합니다."+"\n\n### [ 참고사항 ]\n\n"+"- 지갑은 NFT를 생성 및 보관합니다.\n\n")
    public WalletDto.Response createWallet(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        // 지갑생성하고 멤버필드 업데이트
        return nftApiService.createWallet(member);
    }

    // 토큰 발행
    @PostMapping("/token/{drawingId}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"),  @ExampleObject( name = "400_NFT-002", value = "이미 발행된 토큰입니다."), @ExampleObject( name = "401_NFT-003", value = "본인이 그린 그림만 NFT 발행할 수 있습니다."), @ExampleObject( name = "404_NFT-001", value = "지갑을 찾을 수 없습니다. NFT 지갑발급으로 돌아가 지갑을 생성해 주세요."), @ExampleObject( name = "404_Drawing-001", value = "그림을 찾을 수 없습니다. 그림 id값을 확인해주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
    @Operation(summary = "NFT 토큰 발행", description = "NFT 토큰을 발행합니다."+"\n\n### [ 수행절차 ]\n\n"+"- NFT 발급하고 싶은 그림의 id값을 drawingId에 넣어줍니다.\n\n"+"- Execute 해주세요\n\n"+"- 자기가 그린 그림만 NFT토큰 발행이 가능합니다. \n\n")
    public ResponseEntity<NftResponseDto> createToken(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto){
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        if(member.getSilverCoin() < 1){
            throw new BusinessException(ErrorCode.NOT_ENOUGH_COIN);
        }

        Nft nft = nftApiService.uploadNftAndCreateToken(drawingId, member);
        NftResponseDto nftResponseDto = new NftResponseDto(nft);

        // 은코인 감소, 금코인 증가
        memberCoinService.minusSilverCoin(member, 1);
        memberCoinService.plusGoldCoin(member, 1);
        return ResponseEntity.ok().body(nftResponseDto);
    }

    // 특정 사용자 지갑 조회
    @GetMapping("/wallet/{memberId}")
    @CustomApi
    @Operation(summary = "특정 멤버 NFT 지갑 조회", description = "사용자의 NFT 발행 내역을 조회합니다."+"\n\n### [ 수행절차 ]\n\n"+"- NFT 조회하고 싶은 사용자의 id값을 memberId에 넣어줍니다.\n\n"+"- Execute 해주세요\n\n")
    public TokenListDto.Response getTokenList(@PathVariable Long memberId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        return nftApiService.getTokenList(member);
    }

    // 모든 토큰 조회
    @GetMapping("/token")
    @CustomApi
    @Operation(summary = "발행된 모든 토큰 조회", description = "모든 NFT 토큰들을 조회합니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    public TokenListDto.Response getAllTokenList(){
        return nftApiService.getAllTokenList();
    }

}
