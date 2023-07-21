package com.backend.api.feigntest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExampleCheckDto {

    private String example;
    private List<String> activeProfiles;
}
