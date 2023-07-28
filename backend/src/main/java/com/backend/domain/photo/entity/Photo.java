package com.backend.domain.photo.entity;

import com.backend.domain.bookmark.entity.Bookmarks;
import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.likes.entity.Likes;
import com.backend.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int pickCnt;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int bookmarkCnt;

    @OneToMany(mappedBy = "photo")
    private List<Drawing> drawings;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    private List<Bookmarks> bookmarks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photoFile_id")
    private PhotoFile photoFile;

    // 생성자에서 drawings를 초기화하는 코드 추가
    @Builder
    public Photo(Long photoId, Member member, String photoUrl,
                 int pickCnt, List<Drawing> drawings, PhotoFile photoFile, int bookmarkCnt){
        this.photoId = photoId;
        this.member = member;
        this.photoUrl = photoUrl;
        this.pickCnt = pickCnt;

        // drawings가 null인 경우 빈 리스트로 초기화
        this.drawings = drawings != null ? drawings : new ArrayList<>();

        this.photoFile = photoFile;
        this.bookmarkCnt = bookmarkCnt;
    }

    // setter를 이용해서 bookmarkCnt 업데이트
    public void setBookmarkCnt(int bookmarkCnt) {
        this.bookmarkCnt = bookmarkCnt;
    }

    // 사진으로 그림을 그리면 pickCnt가 증가하는 메소드
     public void incrementPickCnt() {this.pickCnt++;}
}
