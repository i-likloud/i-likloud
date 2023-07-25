package com.backend.domain.drawing.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
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

    private int fromPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drawingFile_id")
    private DrawingFile drawingFile;

    @Builder
    public Drawing(Long drawingId, String title, String artist, String content, String imageUrl, Member member, Photo photo, DrawingFile drawingFile) {
        this.drawingId = drawingId;
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.imageUrl = imageUrl;
        this.member = member;
        this.photo = photo;
        this.drawingFile = drawingFile;
    }

}
