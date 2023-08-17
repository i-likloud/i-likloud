package com.backend.domain.accessory.repository;

import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.member.entity.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    List<Accessory> findByMember(Member member);

    boolean existsByMemberMemberIdAndStoreStoreId(Long memberId, Long storeId);

    @Query("SELECT a.store.storeId FROM Accessory a WHERE a.member.memberId = :memberId")
    Set<Long> findBoughtStoreIdsByMember(@Param("memberId") Long memberId);
}

