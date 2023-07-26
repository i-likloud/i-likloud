package com.backend.domain.photo.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String photoUrl;

    private int pickCnt;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    private List<Drawing> drawings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photoFile_id")
    private PhotoFile photoFile;

    @Builder
    public Photo(Long photoId, Member member, String photoUrl,
                 int pickCnt, List<Drawing> drawings, PhotoFile photoFile){
        this.photoId = photoId;
        this.member = member;
        this.photoUrl = photoUrl;
        this.pickCnt = pickCnt;
        this.drawings = drawings;
        this.photoFile = photoFile;

    }

}
