package com.backend.global.config;

import com.backend.global.jwt.service.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private String refreshTokenExpirationPeriod;


    @Bean
    public TokenManager tokenManager() {
        return new TokenManager(accessTokenExpirationPeriod, refreshTokenExpirationPeriod, secretKey);

    }
}
