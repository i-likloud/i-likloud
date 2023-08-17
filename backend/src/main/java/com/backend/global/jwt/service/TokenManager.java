package com.backend.global.jwt.service;

import com.backend.domain.member.constant.Role;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.AuthenticationException;
import com.backend.global.jwt.constant.GrantType;
import com.backend.global.jwt.constant.TokenType;
import com.backend.global.jwt.dto.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    private final String accessTokenExpirationPeriod;
    private final String refreshTokenExpirationPeriod;
    private final String secretKey;

    public JwtDto createJwtDto(String email, Role role){

        // 토큰 만료 시간
        Date accessTokenExpirationPeriod = createAccessTokenExpirationPeriod();
        Date refreshTokenExpirationPeriod = createRefreshTokenExpirationPeriod();

        String accessToken = createAccessToken(email, role, accessTokenExpirationPeriod);
        String refreshToken = createRefreshToken(email, refreshTokenExpirationPeriod);

        // 액세스, 리프레시 토큰 담아서 반환
        return JwtDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpirationPeriod(accessTokenExpirationPeriod)
                .refreshTokenExpirationPeriod(refreshTokenExpirationPeriod)
                .build();
    }

    public Date createAccessTokenExpirationPeriod() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationPeriod));
    }

    public Date createRefreshTokenExpirationPeriod() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationPeriod));
    }

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email, Role role, Date expirationPeriod) {
        log.info("AccessToken 발급");
        Date now = new Date();
        return Jwts.builder()
                .setSubject(TokenType.ACCESS.name()) // AccessToken
                .setIssuedAt(now) // 발급 시간 설정
                .setExpiration(expirationPeriod) // 만료 시간
                .claim("email", email)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("typ", "JWT")
                .compact();
    }
    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(String email, Date expirationPeriod){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(TokenType.REFRESH.name()) // RefreshToken
                .setIssuedAt(now) // 발급 시간 설정
                .setExpiration(expirationPeriod) // 만료 시간
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.info("token 만료", e);
            throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        return claims;
    }
}
