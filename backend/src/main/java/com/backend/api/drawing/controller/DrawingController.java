package com.backend.api.drawing.controller;

import com.backend.api.drawing.dto.DrawingDetailDto;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.api.drawing.dto.DrawingUploadDto;
import com.backend.api.drawing.service.DrawingUploadService;
import com.backend.api.drawing.service.DrawingViewService;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Drawing", description = "그림 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drawings")
public class DrawingController {

    private final DrawingUploadService drawingUploadService;
    private final DrawingViewService drawingViewService;
    private final MemberService memberService;

    @PostMapping(value = "/upload/from/{photoId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "그림 업로드", description = "그림 파일, 제목, 내용을 요청으로 보내서 그림을 업로드합니다."+"\n\n### [ 수행절차 ]\n\n"+"- 그림 그리는데 사용한 사진의 id값을 photoId에 넣어주세요\n\n"+"- file에 그림을 그린 이미지파일을 선택하여 불러오세요\n\n"+"- 게시글의 제목을 title에 작성해주세요\n\n"+"- 게시글의 내용을 content에 작성해주세요\n\n"+"- Execute 해주세요\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "#### 성공"),
            @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오",
                    content =@Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = { @ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."),
                                    @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"),
                                    @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"),
                                    @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"),
                                    @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."),
                                    @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."),
                                    @ExampleObject( name = "404_Photo-001", value = "사진을 찾을 수 없습니다. 사진 id값을 확인해주세요."),
                                    @ExampleObject( name = "409_File-001", value = "파일 업로드에 실패하였습니다."),
                                    @ExampleObject( name = "409_File-003", value = "파일을 찾을 수 없습니다."),
                                    @ExampleObject( name = "500", value = "서버에러")}))})
    public ResponseEntity<?> uploadDrawing(@RequestPart(value = "file") MultipartFile file,
                                           @RequestPart(value = "title") String title,
                                           @RequestPart(value = "content") String content, @MemberInfo MemberInfoDto memberInfoDto, @PathVariable Long photoId) {
        try {
            DrawingUploadDto uploadDrawing = drawingUploadService.uploadFileAndCreateDrawings(file, title, content, memberInfoDto, photoId);
            return ResponseEntity.ok().body(uploadDrawing);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @CustomApi
    @Operation(summary = "그림 게시물 전체 조회", description = "그림 게시물들을 전체 조회합니다."+"\n\n### [ 참고사항 ]\n\n"+"- 바로 Execute 할 경우 최신순으로 정렬됩니다.(기본값) orderBy 값이 createdAt인지 확인하세요\n\n"+"- 좋아요 순으로 조회하고 싶다면 orderBy 값에 likesCount 라고 변경해주세요\n\n"+"- 조회수 순으로 조회하고 싶다면orderBy 값에 viewCount 라고 변경해주세요\n\n"+"- Execute 해주세요\n\n")
    public ResponseEntity<List<DrawingListDto>> drawingBoard(@MemberInfo MemberInfoDto memberInfoDto, @RequestParam(defaultValue = "createdAt") String orderBy){
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        List<DrawingListDto> drawings = drawingViewService.getAllDrawings(member, orderBy);
        return ResponseEntity.ok().body(drawings);
    }

    @GetMapping("/{drawingId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "#### 성공"),
            @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오",
                    content =@Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {@ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."),@ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"),
                                    @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"),
                                    @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"),
                                    @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."),
                                    @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."),
                                    @ExampleObject( name = "404_Drawing-001", value = "그림을 찾을 수 없습니다. 그림 id값을 확인해주세요."),
                                    @ExampleObject( name = "500", value = "서버에러")}))})
    @Operation(summary = "그림 게시물 상세 조회", description = "그림 게시물을 상세 조회합니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    public ResponseEntity<DrawingDetailDto> getDrawing(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        DrawingDetailDto drawing = drawingViewService.getDrawing(drawingId, member.getMemberId());
        return ResponseEntity.ok(drawing);
    }

    @DeleteMapping("/delete/{drawingId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "#### 성공"),
            @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오",
                    content =@Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject( name = "400_User-004", value = "해당 회원은 존재하지 않습니다."),
                                     @ExampleObject( name = "401_Auth-001", value = "토큰이 만료되었습니다. 토큰을 재발급 받아주세요"),
                                    @ExampleObject( name = "401_Auth-004", value = "해당 토큰은 ACCESS TOKEN이 아닙니다. 토큰값이 추가정보 기입에서 받은 new token 값이 맞는지 확인해주세요"),
                                    @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"),
                                    @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 access token값을 넣어주세요."),
                                    @ExampleObject( name = "401_Auth-010", value = "본인이 올린 그림만 삭제할 수 있습니다. 기입하신 그림id에 해당하는 그림이 본인이 올린것인지 확인해주세요."),
                                    @ExampleObject( name = "403_Auth-009", value = "회원이 아닙니다. 추가정보로 이동하여 추가정보를 입력해 주세요."),
                                    @ExampleObject( name = "404_Drawing-001", value = "그림을 찾을 수 없습니다. 그림 id값을 확인해주세요."),
                                    @ExampleObject( name = "500", value = "서버에러")}))})
    @Operation(summary = "그림 게시물 삭제", description = "그림 게시물을 삭제합니다. "+"\n\n### [ 수행절차 ]\n\n"+"- 삭제하고 싶은 그림의 id값을 drawingId에 넣어주세요\n\n"+"- Execute 해주세요\n\n"+"- 본인이 작성한 게시물만 삭제할 수 있습니다.\n\n")
    public ResponseEntity<String> deleteDrawing(@PathVariable Long drawingId, @MemberInfo MemberInfoDto memberInfoDto) {
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
        drawingViewService.deleteDrawing(drawingId, member.getMemberId());
        return ResponseEntity.ok(String.format("%d번 게시물 삭제ㅠㅠ", drawingId));
    }
}
