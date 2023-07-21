package com.backend.api.feigntest.controller;

import com.backend.api.feigntest.dto.ExampleCheckDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExampleCheckController {

    private final Environment environment;

    @GetMapping("/feign")
    public ResponseEntity<ExampleCheckDto> exampleCheck() {
        ExampleCheckDto exampleCheckDto = ExampleCheckDto.builder()
                .example("ok")
                .activeProfiles(Arrays.asList(environment.getActiveProfiles()))
                .build();
        return ResponseEntity.ok(exampleCheckDto);
    }
}
