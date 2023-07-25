package com.backend.domain.photo.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @ManyToOne
    private Member memberId;


    private String photoUrl;

    private int pickCnt;

    @Builder
    public Photo(Member memberId, String photoUrl, int pickCnt){
        this.memberId = memberId;
        this.photoUrl = photoUrl;
        this.pickCnt = pickCnt;
    }

}
