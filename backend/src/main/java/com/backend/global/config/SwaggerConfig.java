package com.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("")
                .pathsToMatch("/**")
                .build();
    }
    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("소셜로그인")
                .pathsToMatch("/api/oauth/**")
                .build();
    }
    @Bean
    public GroupedOpenApi mypageApi() {
        return GroupedOpenApi.builder()
                .group("마이페이지")
                .pathsToMatch("/api/mypage/**")
                .build();
    }
    @Bean
    public GroupedOpenApi StoreApi() {
        return GroupedOpenApi.builder()
                .group("상점")
                .pathsToMatch("/api/store/**")
                .build();
    }
    @Bean
    public GroupedOpenApi tokenApi() {
        return GroupedOpenApi.builder()
                .group("토큰")
                .pathsToMatch("/api/accounts/**")
                .build();
    }
    @Bean
    public GroupedOpenApi drawingsApi() {
        return GroupedOpenApi.builder()
                .group("그림")
                .pathsToMatch("/api/drawings/**")
                .build();
    }

    @Bean
    public GroupedOpenApi photoApi() {
        return GroupedOpenApi.builder()
                .group("사진")
                .pathsToMatch("/api/photo/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder()
                .group("댓글")
                .pathsToMatch("/api/comment/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("회원")
                .pathsToMatch("/api/member/**")
                .build();
    }

    @Bean
    public GroupedOpenApi visionApi() {
        return GroupedOpenApi.builder()
                .group("GCP")
                .pathsToMatch("/api/vision/**")
                .build();
    }
    @Bean
    public GroupedOpenApi walletApi() {
        return GroupedOpenApi.builder()
                .group("지갑")
                .pathsToMatch("/api/wallet/**")
                .build();
    }
    @Bean
    public GroupedOpenApi reportApi() {
        return GroupedOpenApi.builder()
                .group("신고")
                .pathsToMatch("/api/report/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .info(new Info().title("💧운이 좋아🌥")
                        .description("<뭉게뭉게 도화지> 프로젝트 API")
                        .version("v2.7.5"));
    }

}