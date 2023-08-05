package com.backend.domain.history.entity;

import com.backend.domain.common.BaseEntity;
import com.backend.domain.history.constant.HistoryType;
import com.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryType historyType;

    public History(String content, HistoryType historyType){
        this.content = content;
        this.historyType = historyType;
    }
}
