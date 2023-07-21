package com.backend.api.feigntest.controller;


import com.backend.api.feigntest.cilent.ExampleFeignClient;
import com.backend.api.feigntest.dto.ExampleCheckDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExampleFeignClientTest {

    private final ExampleFeignClient exampleFeignClient;

    @GetMapping("/feign/test")
    public ResponseEntity<ExampleCheckDto> exampleCheckTest() {
        ExampleCheckDto exampleCheckDto = exampleFeignClient.exampleCheck();
        return ResponseEntity.ok(exampleCheckDto);
    }

}
