package com.backend.global.firebase.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private Long targetUserId;
    private String title;
    private String body;
    private Long drawingId;
//    private String image;
//    private Map<String, String> data;

    @Builder
    public FCMNotificationRequestDto(Long targetUserId, String title, String body, Long drawingId) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
        this.drawingId = drawingId;
    }
}
