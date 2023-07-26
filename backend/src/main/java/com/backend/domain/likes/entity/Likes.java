package com.backend.domain.likes.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

}
