package com.backend.domain.accessory.repository;

import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    List<Accessory> findByMember(Member member);

    boolean existsByMemberMemberIdAndStoreStoreId(Long memberId, Long storeId);
}

