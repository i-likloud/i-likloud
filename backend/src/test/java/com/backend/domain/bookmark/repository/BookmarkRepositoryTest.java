package com.backend.domain.bookmark.repository;

import com.backend.api.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class BookmarkRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    void findByMemberMemberIdAndPhotoPhotoId() {
    }

    @Test
    void findByMember() {
    }

    @Test
    void deleteBookmarksByPhotoId() {
    }
}