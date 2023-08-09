package com.backend.api.drawing.service;

import com.backend.api.common.BaseIntegrationTest;
import com.backend.api.drawing.dto.DrawingDetailDto;
import com.backend.api.drawing.dto.DrawingFromPhotoDto;
import com.backend.api.drawing.dto.DrawingListDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.likes.repository.LikesRepository;
import com.backend.domain.member.entity.Member;
import com.backend.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class DrawingViewServiceTest extends BaseIntegrationTest {

    @Autowired
    DrawingViewService drawingViewService;

    @MockBean
    DrawingRepository drawingRepository;

    @MockBean
    LikesRepository likesRepository;

    Drawing drawing1 = null;
    Drawing drawing2 = null;

    @BeforeEach
    void setUp() {

        drawing1 = Drawing.builder()
                .title("title")
                .artist("yt")
                .member(new Member())
                .imageUrl("123")
                .content("content")
                .nftYn(false)
                .likesCount(45)
                .viewCount(0)
                .build();


        drawing2 = Drawing.builder()
                .title("title2")
                .artist("yt")
                .member(new Member())
                .imageUrl("456")
                .content("content2")
                .nftYn(true)
                .likesCount(200)
                .viewCount(53)
                .build();

    }

    @Test
    public void getAllDrawings_최신순_테스트() {

        // given
        String orderBy = "createdAt";

        when(drawingRepository.findAll(Sort.by(Sort.Direction.DESC, orderBy)))
                .thenReturn(Arrays.asList(drawing1, drawing2));

        // when
        List<DrawingListDto> drawings = drawingViewService.getAllDrawings(orderBy);

        // then
        assertThat(drawings.size()).isEqualTo(2);

        assertThat(drawings.get(0)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing1));
        assertThat(drawings.get(1)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing2));

    }

    @Test
    public void getAllDrawings_조회순_테스트() {

        // given
        String orderBy = "viewCount";

        when(drawingRepository.findAll(Sort.by(Sort.Direction.DESC, orderBy)))
                .thenReturn(Arrays.asList(drawing2, drawing1));

        // when
        List<DrawingListDto> drawings = drawingViewService.getAllDrawings(orderBy);

        // then
        assertThat(drawings.size()).isEqualTo(2);

        assertThat(drawings.get(0)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing2));
        assertThat(drawings.get(1)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing1));

    }

    @Test
    public void getAllDrawings_좋아요순_테스트() {
        // given
        String orderBy = "likesCount";

        when(drawingRepository.findAll(Sort.by(Sort.Direction.DESC, orderBy)))
                .thenReturn(Arrays.asList(drawing2, drawing1));

        // when
        List<DrawingListDto> drawings = drawingViewService.getAllDrawings(orderBy);

        // then
        assertThat(drawings.size()).isEqualTo(2);

        assertThat(drawings.get(0)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing2));
        assertThat(drawings.get(1)).usingRecursiveComparison().isEqualTo(new DrawingListDto(drawing1));

    }


    @Test
    public void getDrawing_그림상세_테스트() {

        Long drawingId = 1L;
        Long memberId = 1L;
        drawing1.setViewCount(10); // 초기 조회수

        // given
        when(drawingRepository.findByDrawingIdWithComments(drawingId))
                .thenReturn(Optional.ofNullable(drawing1));

        when(likesRepository.existsByMemberMemberIdAndDrawingDrawingId(memberId, drawingId))
                .thenReturn(true); // 좋아요 눌렀다고 가정

        // when
        DrawingDetailDto drawing = drawingViewService.getDrawing(drawingId, memberId);

        // then
        assertThat(drawing.getTitle()).isEqualTo(drawing1.getTitle());
        assertThat(drawing.getContent()).isEqualTo(drawing1.getContent());
        assertThat(drawing.getArtist()).isEqualTo(drawing1.getArtist());
        assertThat(drawing.isNftYn()).isEqualTo(drawing1.isNftYn());
        assertThat(drawing.isMemberLiked()).isEqualTo(true);
        assertThat(drawing.getViewCount()).isEqualTo(drawing1.getViewCount());
        assertThat(drawing.getLikesCount()).isEqualTo(drawing1.getLikesCount());

    }

    @Test
    public void editDrawing_그림수정_테스트() {

        // given
        Long drawingId = 1L;
        Member member = new Member();
        member.setMemberId(1L);
        drawing1.setMember(member);

        String updatedTitle = "수정된 제목";
        String updatedContent = "수정된 내용";

        when(drawingRepository.findById(drawingId))
                .thenReturn(Optional.ofNullable(drawing1));

        // when
        DrawingFromPhotoDto updateDrawing = drawingViewService.editDrawing(drawingId, member, updatedTitle, updatedContent);

        // then
        assertThat(updateDrawing.getTitle()).isEqualTo(updatedTitle);
        assertThat(updateDrawing.getContent()).isEqualTo(updatedContent);

    }

    @Test
    public void editDrawing_그림수정_UNAUTHORIZED_테스트() {

        // given
        Long drawingId = 1L;
        Member member = new Member();
        member.setMemberId(1L);

        Member anotherMember = new Member();
        anotherMember.setMemberId(2L);

        drawing1.setMember(anotherMember);

        when(drawingRepository.findById(drawingId))
                .thenReturn(Optional.ofNullable(drawing1));

        // then
        assertThrows(BusinessException.class, () -> {
            //when
            drawingViewService.editDrawing(drawingId, member, "수정제목", "수정내용");
        });
    }

    @Test
    @Transactional
    public void deleteDrawing_그림삭제_테스트() {

        // given
        Long drawingId = 1L;
        Long memberId = 1L;

        Member member = new Member();
        member.setMemberId(memberId);

        drawing1.setMember(member);

        when(drawingRepository.findById(drawingId))
                .thenReturn(Optional.ofNullable(drawing1));

        // when
        drawingViewService.deleteDrawing(drawingId, memberId);

        // then
        verify(drawingRepository).delete(drawing1);

    }

    @Test
    public void deleteDrawing_그림삭제_UNAUTHORIZED_테스트() {

        // given
        Long drawingId = 1L;
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        Member anotherMember = new Member();
        anotherMember.setMemberId(2L);

        drawing1.setMember(anotherMember);

        when(drawingRepository.findById(drawingId))
                .thenReturn(Optional.ofNullable(drawing1));

        // then
        assertThrows(BusinessException.class, () -> {
            //when
            drawingViewService.deleteDrawing(drawingId, memberId);
        });
    }
}
