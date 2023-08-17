package com.backend.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // Jpa Auditing Listener
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @CreatedDate // 생성시간 자동 설정
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시간 자동 설정
    private LocalDateTime updatedAt;
}
