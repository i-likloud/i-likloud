package com.backend.domain.accessory.repository;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.domain.member.constant.Role;
import com.backend.domain.member.constant.SocialType;
import com.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.backend.domain.accessory.entity.Accessory;
import com.backend.domain.member.entity.Member;
import com.backend.domain.store.entity.Store;
import com.backend.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AccessoryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 테스트 데이터
    private Member member;
    private Store store;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 초기화
        member = new Member(SocialType.KAKAO,"test@example.com", "TestUser", 0,0,0,0,0, Role.MEMBER);
        member = memberRepository.save(member);

        store = new Store(1L,"test",0);
        store = storeRepository.save(store);
    }

    @AfterEach
    public void tearDown() {
        // 테스트 데이터 제거
        accessoryRepository.deleteAll();
        storeRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    public void findByMember_ShouldReturnAccessoriesOfMember() {
        // Given
        Accessory accessory1 = new Accessory(1L, member, store);
        accessoryRepository.save(accessory1);

        Accessory accessory2 = new Accessory(2L, member, store);
        accessoryRepository.save(accessory2);

        Accessory accessory3 = new Accessory(3L, member, store);
        accessoryRepository.save(accessory3);

        // When
        List<Accessory> accessories = accessoryRepository.findByMember(member);



        // Then
        // 검증 단계
        // 예상한 악세사리 리스트를 생성합니다.
        List<Accessory> expectedAccessories = Arrays.asList(accessory1, accessory2, accessory3);

        // 두 리스트의 크기를 비교합니다.
        assertEquals(expectedAccessories.size(), accessories.size());

//        // 두 리스트의 크기를 비교하여 출력합니다.
//        System.out.println("Expected Size: " + expectedAccessories.size());
//        System.out.println("Actual Size: " + accessories.size());
//
//        // 예상한 악세사리 리스트의 내용을 출력합니다.
//        System.out.println("Expected Accessories:");
//        for (Accessory expectedAccessory : expectedAccessories) {
//            System.out.println(" - " + expectedAccessory);
//        }

        // 조회된 악세사리 리스트의 내용을 출력합니다.
        System.out.println("Actual Accessories:");
        for (Accessory actualAccessory : accessories) {
            System.out.println(" - " + actualAccessory);
        }

//        // 두 리스트의 내용을 비교합니다. (순서가 보장되지 않을 수 있으므로 순서를 무시하고 비교합니다.)
//        assertTrue(accessories.contains(accessory1));
//        assertTrue(accessories.contains(accessory2));
//        assertTrue(accessories.contains(accessory3));
        assertTrue(accessories.containsAll(expectedAccessories));
    }

    @Test
    public void existsByMemberMemberIdAndStoreStoreId_ShouldReturnTrueIfAccessoryExists() {
        // Given
        Accessory accessory = new Accessory(1L, member, store);
        accessoryRepository.save(accessory);

        // When
        boolean exists = accessoryRepository.existsByMemberMemberIdAndStoreStoreId(member.getMemberId(), store.getStoreId());

        // Then
        assertTrue(exists);
    }

    @Test
    public void existsByMemberMemberIdAndStoreStoreId_ShouldReturnFalseIfAccessoryDoesNotExist() {
        // When
        boolean exists = accessoryRepository.existsByMemberMemberIdAndStoreStoreId(member.getMemberId(), store.getStoreId());

        // Then
        assertFalse(exists);
    }

    //에러 테스트코드 추가
    @Test
    public void findByMember_ShouldThrowException() {
        // Given
        Member nonExistentMember = new Member(); // 가짜 Member 객체

        // When & Then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> accessoryRepository.findByMember(nonExistentMember));
    }
}
