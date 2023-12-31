package com.backend.api.member.controller;

import com.backend.api.member.dto.MemberDto;
import com.backend.api.member.dto.MemberFindDto;
import com.backend.api.member.dto.MemberInfoResponseDto;
import com.backend.api.member.dto.MemberSearchDto;
import com.backend.api.member.service.MemberInfoService;
import com.backend.api.token.dto.AccessTokenResponseDto;
import com.backend.api.token.service.TokenService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.member.service.MemberService;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Tag(name = "Member", description = "회원 관련 api")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberInfoController {

    private final TokenService tokenService;
    private final MemberInfoService memberInfoService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 회원 정보 조회
    @Operation(summary = "회원 정보", description = "회원가입후 회원 정보 조회 메서드입니다."+"\n\n### [ 참고사항 ]\n\n"+"- try it out 해주세요\n\n")
    @CustomApi
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(@MemberInfo MemberInfoDto memberInfoDto) {

        String email = memberInfoDto.getEmail();
        MemberInfoResponseDto memberInfoResponseDto = memberInfoService.getMemberInfo(email);

        return ResponseEntity.ok(memberInfoResponseDto);
    }

    // 추가정보 업데이트 메서드
    @Operation(summary = "추가 정보", description = "회원가입후 추가정보 관련 메서드입니다. \n\n " +"\n\n### [ 수행절차 ]\n\n"+"- login 에서 발급 받은 access-token을 자물쇠에 넣고 Execute 해주세요. (request body는 아래에 예시값의 request값만 사용해주세요.)\n\n"+ "- Response body의 accessToken 또는 Response headers의 newtoken을 복사하여 새로 자물쇠에 넣어 주세요 \n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = { @ExampleObject(name="400_User-003", value ="이미 등록된 닉네임입니다. 다른 닉네임을 기입해 주세요"), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
    @PatchMapping("/additional")
    public ResponseEntity<AccessTokenResponseDto> updateAdditionalInfo(@RequestBody MemberDto.UpdateRequest request, @MemberInfo MemberInfoDto memberInfoDto) {
        // 닉네임 중복 검사
        Optional<Member> findByNickname = memberRepository.findByNickname(request.getNickname());
        if (findByNickname.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_NICKNAME);
        }
        MemberDto.Response response = memberInfoService.updateAdditionalInfo(request, memberInfoDto.getEmail());
        // 이메일로 멤버 찾아서 refresh token 가져오기
        String refreshToken = memberService.findMemberByEmail(memberInfoDto.getEmail()).getRefreshToken();
        // 엑세스 토큰 재발급
        AccessTokenResponseDto newToken = tokenService.createAccessTokenByRefreshToken(refreshToken);

        // 본문에는 response, 헤더에는 새 토큰을 추가하여 ResponseEntity 생성
        return ResponseEntity.ok()
                .header("newToken", newToken.getAccessToken())
                .body(newToken);
    }

    // 특정 멤버 조회
    @Operation(summary = "특정 멤버 정보", description = "회원가입후 회원 정보 조회 메서드입니다."+"\n\n### [ 수행절차 ]\n\n"+"- 조회하고자 하는 member의 id값을 넣어주세요\n\n"+"- Execute 해주세요\n\n")
    @CustomApi
    @GetMapping("/search/{memberId}")
    public ResponseEntity<MemberSearchDto> memberSearchInfo(@PathVariable Long memberId) {
        Member member = memberService.findMemberById(memberId);
        MemberSearchDto memberInfo = memberInfoService.getMember(member);
        return ResponseEntity.ok(memberInfo);
    }

    // 멤버 검색
//    @Operation(summary = "사용자 검색 페이지", description = "NFT선물을 위해 보낼 사람 검색하는 페이지 입니다."+"\n\n### [ 참고사항 ]\n\n"+"- 처음에는 모든 유저를 보여줍니다.\n\n"+"- 이후 아래 사용자 검색을 통해 닉네임을 검색하면 입력값을 포함한 값만 나타냅니다.\n\n")
//    @CustomApi
//    @GetMapping("/search/home")
//    public ResponseEntity<List<MemberSearchDto>> memberSerach(){
//        List<Member> members = memberService.findList();
//        List<MemberSearchDto> membersList = memberInfoService.getMemberList(members);
//        return ResponseEntity.ok(membersList);
//    }

    //닉네임으로 맴버 검색
    @Operation(summary = "사용자 검색", description = "NFT선물을 위해 보낼 사람 검색하는 기능입니다."+"\n\n### [ 수행절차 ]\n\n"+"- 닉네임에 포함되어야 되는 텍스트값을 입력합니다.\n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다. 다른 닉네임으로 검색해주세요. DB에 저장된 닉네임만 검색가능합니다."), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "500", value = "서버에러")}))}) @PostMapping("/search/{nickname}")
    public ResponseEntity<List<MemberFindDto>> memberSerachTogive(@PathVariable(required = false) String nickname){
        List<Member> members = memberService.findMemberByNickname(nickname);
        
        // 닉네임 길이별로 오름차순 정렬
        members.sort(Comparator.comparingInt(member -> member.getNickname().length()));

        List<MemberFindDto> memberSearchList = memberInfoService.getMemberList(members);
        return ResponseEntity.ok(memberSearchList);
    }
}
