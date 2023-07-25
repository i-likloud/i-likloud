package com.backend.domain.member.controller;

import com.backend.domain.member.dto.MypageInfoDto;
import com.backend.domain.member.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Mypage", description = "마이페이지 관련 api")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;


    @Operation(summary = "프로필 조회", description = "마이 프로필 페이지 조회 메서드입니다.")
    @GetMapping("/me") //마이페이지 조회
    public ResponseEntity<MypageInfoDto> getMyInfo(HttpServletRequest request){
        try {
            String userEmail = request.getRemoteUser(); // 현재 사용자 조회
            MypageInfoDto myPageInfoDto = mypageService.getMyInfo(userEmail);
            return ResponseEntity.ok(myPageInfoDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @Operation(summary = "프로필", description = "프로필  메서드입니다.")
//    @GetMapping("/me") //마이페이지 조회

}
