package com.backend.domain.photo.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.drawing.entity.Drawing;
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
@AllArgsConstructor
@Builder
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String photoUrl;

    private int pickCnt;

    @Builder.Default
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    private List<Drawing> drawings = new ArrayList<>();



}
