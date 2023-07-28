package com.backend.api.comment.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequsetDto {

    private Long commentId;
    private String content;

}
