package com.backend.api.token.controller;

import com.backend.api.token.dto.AccessTokenResponseDto;
import com.backend.api.token.service.TokenService;
import com.backend.global.error.ErrorResponse;
import com.backend.global.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Token", description = "토큰 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class TokenController {

    private final TokenService tokenService;

    // refresh token 날아감
    @Operation(summary = "토큰 재발급", description = "access token 재발급 관련 메서드입니다."+"\n\n### [ 수행절차 ]\n\n"+"- try it out 해주세요\n\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "#### 성공"),
            @ApiResponse(responseCode = "에러", description = "#### 에러 이유를 확인 하십시오",
                    content =@Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = { @ExampleObject( name = "401_Auth-002", value = "해당 refresh token이 존재하지 않습니다. 토큰값이 refresh token 값이 맞는지 확인해주세요."),
                                    @ExampleObject( name = "401_Auth-003", value = "해당 refresh token은 만료되었습니다. 새로 로그인 해주세요."),
                                    @ExampleObject( name = "401_Auth-005", value = "해당 토큰은 유효한 토큰이 아닙니다. 추가정보 기입에서 받은 new token 값을 넣어주세요"),
                                    @ExampleObject( name = "401_Auth-006", value = "Authorization Header가 없습니다. 자물쇠에 refresh token값을 넣어주세요."),
                                    @ExampleObject( name = "500", value = "서버에러")}))})
    @PostMapping("/access-token/re")
    public ResponseEntity<AccessTokenResponseDto> createAccessToken(HttpServletRequest httpServletRequest) {
        // 헤더 검증
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

        // refresh 추출 후 토큰 재발급으로 이동
        String refreshToken = authorizationHeader.split(" ")[1];
        AccessTokenResponseDto accessTokenResponseDto = tokenService.createAccessTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok(accessTokenResponseDto);
    }
}
