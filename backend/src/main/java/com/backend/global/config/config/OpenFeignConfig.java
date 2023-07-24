package com.backend.global.config.config;

import com.backend.global.error.FeignClientErrorException;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = "com.backend")
@Import(FeignClientsConfiguration.class)
public class OpenFeignConfig {

    @Bean
    // 요청과 응답에 대한 헤더와 바디, 메타 데이터를 남김
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder(){
        return new FeignClientErrorException();
    }

    @Bean
    Retryer.Default retryer() {
        // 0.5초의 간격으로 시작해 최대 3초의 간격으로 점점 증가하며, 최대5번 재시도한다.
        return new Retryer.Default(500L, TimeUnit.SECONDS.toMillis(3L), 5);
    }
}
