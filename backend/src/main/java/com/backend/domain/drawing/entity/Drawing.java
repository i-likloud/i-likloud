package com.backend.domain.drawing.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
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
public class Drawing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawingId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String artist;

    @Column(length = 300)
    private String content;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Builder
    public Drawing(String title, String content, String imageUrl, Member member){
        this.title = title;
        this.content = content;
        this.artist = member.getNickname();
        this.imageUrl = imageUrl;

    }
}
