package com.backend.api.feigntest.cilent;


import com.backend.api.feigntest.dto.ExampleCheckDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "exampleClient", url = "http://localhost:8080/")
public interface ExampleFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/feign", consumes = "application/json")
    ExampleCheckDto exampleCheck();
}