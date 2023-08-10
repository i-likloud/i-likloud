package com.backend.domain.comment.entity;


import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String commentMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawing_id")
    private Drawing drawing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 100)
    private String content;

    public Comment(String commentMember, Drawing drawing, Member member, String content){
        this.commentMember = commentMember;
        this.drawing =drawing;
        this.member = member;
        this.content = content;
    }

}
