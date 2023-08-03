package com.backend.api.photo.controller;

import com.backend.api.drawing.dto.DrawingWithBookmarksDto;
import com.backend.api.photo.dto.PhotoDetailDto;
import com.backend.api.photo.dto.PhotoInfoResponseDto;
import com.backend.api.photo.service.PhotoInfoService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.global.error.ErrorResponse;
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

import java.util.List;

@Tag(name = "Photo", description = "사진 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/photo")
public class PhotoInfoController {

    private final PhotoInfoService photoInfoService;
    private final MemberService memberService;


    //전체 조회(최신순)
    @Operation(summary = "전체 조회(최신순)", description = "DB에 있는 모든 사진을 최신순으로 조회합니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    @CustomApi
    @GetMapping("/new")
    public List<PhotoInfoResponseDto> searchAllDesc(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.searchAllDesc(member);
    }

    //전체 조회(그림그린순)
    @Operation(summary = "전체 조회(그림 많이 그린 순)", description = "DB에 있는 모든 사진을 그림 많이 그린 순(많이 사용한 순)으로 조회합니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    @CustomApi
    @GetMapping("/pick")
    public List<PhotoInfoResponseDto> searchAllPickCntDesc(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.searchAllPickCntDesc(member);
    }

    //전체 조회(북마크 순)
    @Operation(summary = "전체 조회(즐겨찾기순)", description = "DB에 있는 모든 사진을 즐겨찾기 많이 한 순으로 조회합니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    @CustomApi
    @GetMapping("/bookmarkdesc")
    public List<PhotoInfoResponseDto> searchAllBookmarkCntDesc(@MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.searchAllBookmarkCntDesc(member);
    }

    //사진 상세 조회
    @Operation(summary = "사진 상세 조회", description = "사진을 상세조회 합니다."+"\n\n### [ 수행절차 ]\n\n"+"- 조회하고 싶은 사진의 id값을 photoId에 넣어주세요\n\n"+"- Execute 해주세요\n\n"+"\n\n### [ 참고사항 ]\n\n"+"- photo_url, 사진id, 멤버id 값을 반환합니다.\n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "404_Photo-001", value = "사진을 찾을 수 없습니다. 사진 id값을 확인해주세요."), @ExampleObject( name = "500", value = "서버에러")}))})

    @GetMapping("/{photoId}")
    public PhotoDetailDto photoDetail(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        return photoInfoService.getPhotoDetail(photoId, member);
    }

    // 클릭한 photoId와 관련된 모든 drawings 조회
    @Operation(summary = "특정 사진 이용한 모든 그림", description = "해당하는 사진을 이용해 그림을 그린 모든 그림리스트를 출력합니다."+"\n\n### [ 수행절차 ]\n\n"+"- 조회하고 싶은 사진의 id값을 photoId에 넣어주세요\n\n"+"- Execute 해주세요\n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "404_Photo-001", value = "사진을 찾을 수 없습니다. 사진 id값을 확인해주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
    @GetMapping("/{photoId}/alldrawings")
    public ResponseEntity<List<DrawingWithBookmarksDto>> getDrawingsByPhotoId(@PathVariable Long photoId) {
        List<DrawingWithBookmarksDto> drawings = photoInfoService.getDrawingsByPhotoId(photoId);
        return ResponseEntity.ok(drawings);
    }

    // 삭제
    @Operation(summary = "사진 삭제", description = "사진을 삭제합니다."+"\n\n### [ 수행절차 ]\n\n"+"- 삭제하고 싶은 사진의 id값을 photoId에 넣어주세요\n\n"+"- Execute 해주세요\n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."), @ExampleObject( name = "404_Photo-001", value = "사진을 찾을 수 없습니다. 사진 id값을 확인해주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "401_Auth-010", value = "본인이 올린 사진만 삭제할 수 있습니다. 기입하신 사진id에 해당하는 사진이 본인이 올린것인지 확인해주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> photoDelete(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto){
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        photoInfoService.delete(photoId, member);

        return ResponseEntity.ok(String.format("%d번 사진 삭제ㅠㅠ", photoId));
    }

    // 사진 즐겨찾기 토글
    @Operation(summary = "즐겨찾기 추가", description = "사진을 즐겨찾기 또는 즐겨찾기 해제 합니다."+"\n\n### [ 수행절차 ]\n\n"+"- 즐겨찾기 하고 싶은 사진의 id값을 photoId에 넣어주세요\n\n"+"- Execute 해주세요\n\n"+"- 이미 즐겨찾기 되어 있다면 즐겨찾기가 해제 됩니다.\n\n"+"- 즐겨찾기가 안되어 있다면 즐겨찾기 됩니다.\n\n")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "#### 성공"), @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오", content =@Content(schema = @Schema(implementation = ErrorResponse.class), examples = {@ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."), @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"), @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"), @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"), @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."), @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."), @ExampleObject( name = "404_Photo-001", value = "사진을 찾을 수 없습니다. 사진 id값을 확인해주세요."), @ExampleObject( name = "500", value = "서버에러")}))})
>>>>>>> backend/src/main/java/com/backend/api/photo/controller/PhotoInfoController.java
    @PostMapping("/{photoId}/bookmark")
    public ResponseEntity<String> pickPhoto(@PathVariable Long photoId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        if(photoInfoService.isAlreadyBookmarked(member, photoId)) {
            photoInfoService.unpickPhoto(photoId, member.getMemberId());
            return  ResponseEntity.ok(String.format("%d번 사진 북마크 취소", photoId));
        }
        else {
            photoInfoService.pickPhoto(photoId, member.getMemberId());
            return  ResponseEntity.ok(String.format("%d번 사진 북마크", photoId));
        }
    }

}

