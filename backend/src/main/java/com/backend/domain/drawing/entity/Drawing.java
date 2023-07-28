package com.backend.domain.drawing.entity;

import com.backend.domain.comment.entity.Comment;
import com.backend.domain.common.BaseEntity;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drawingFile_id")
    private DrawingFile drawingFile;

    @OneToMany(mappedBy = "drawing", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "drawing", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public Drawing(Long drawingId, String title, String artist, String content, String imageUrl,
                   Member member, Photo photo, DrawingFile drawingFile, int viewCount, int likesCount) {
        this.drawingId = drawingId;
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.viewCount = viewCount;
        this.likesCount = likesCount;
        this.imageUrl = imageUrl;
        this.member = member;
        this.photo = photo;
        this.drawingFile = drawingFile;
    }

}
