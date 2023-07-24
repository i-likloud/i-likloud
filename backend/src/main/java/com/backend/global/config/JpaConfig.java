package com.backend.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    // Jpa Auditing 설정
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {

            @Autowired
            private HttpServletRequest httpServletRequest;

            @Override
            public Optional<String> getCurrentAuditor() {
                String modifiedBy = httpServletRequest.getRequestURI(); // 수정 경로 뽑아내기
                // 수정 경로가 없으면 "unknown" 으로 설정
                if(!StringUtils.hasText(modifiedBy)) {
                    modifiedBy = "unknown";
                }
                return Optional.of(modifiedBy);
            }
        };
    }
}
