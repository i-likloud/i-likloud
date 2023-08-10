package com.backend.domain.bookmark.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.entity.Photo;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Bookmarks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public Bookmarks(Member member, Photo photo) {
        this.member = member;
        this.photo = photo;
    }

}
