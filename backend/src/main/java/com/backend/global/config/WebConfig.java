package com.backend.global.config;

import com.backend.global.interceptor.AdminAuthorizationInterceptor;
import com.backend.global.interceptor.AuthenticationInterceptor;
import com.backend.global.interceptor.MemberAuthorizationInterceptor;
import com.backend.global.resolver.memberInfo.MemberInfoArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final MemberInfoArgumentResolver memberInfoArgumentResolver;
    private final MemberAuthorizationInterceptor memberAuthorizationInterceptor;
    private final ObjectMapper objectMapper;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 어떤 api 경로에 매핑할지
//                .allowedOrigins("http://localhost:8082") // 어떤 경로에서 오는걸 cors 허용할지 , 콤마 써서 여러개 쓸 수 있음
                .allowedOrigins("*")
                .allowedMethods( // 허용할 http 메소드
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
                )
                .maxAge(3600); // preflight 시간 설정
    }

    // HTTP 요청을 처리하는 과정에 인터셉터를 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .order(1) // 가장 먼저 인증 인터셉터가 실행
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/oauth/login",
                        "/api/accounts/access-token/re",
                        "/api/oauth/logout",
                        "/api/feign/**");

        registry.addInterceptor(memberAuthorizationInterceptor)
                .order(2)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/member/additional",
                        "/api/accounts/access-token/re",
                        "/api/oauth/logout",
                        "/api/oauth/login");

//        registry.addInterceptor(adminAuthorizationInterceptor)
//                .order(2)
//                .addPathPatterns("/api/admin/**");
    }

    // 컨트롤러 메서드의 매개변수를 해석 ( JWT 해석해서 정보 추출)
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
    }
}