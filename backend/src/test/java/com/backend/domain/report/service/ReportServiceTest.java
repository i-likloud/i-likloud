package com.backend.domain.report.service;

import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.report.entity.Report;
import com.backend.domain.report.repository.ReportRepository;
import com.backend.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private DrawingRepository drawingRepository;

    @InjectMocks
    private ReportService reportService;

    private Member member;
    private Drawing drawing;

    @BeforeEach
    public void setUp() {
        // 테스트에 사용할 Member 객체와 Drawing 객체 생성
        member = new Member();
        member.setMemberId(1L);

        drawing = new Drawing();
        drawing.setDrawingId(10L);
    }

    @Test
    public void testCreateReport_Success() {
        // DrawingRepository의 findById 메서드를 Mock으로 대체하여 가짜 Drawing 객체 반환하도록 설정
        // 어떤 Long 타입을 보내도 drawing 객체 반환하도록
        when(drawingRepository.findById(anyLong())).thenReturn(Optional.of(drawing));

        String content = "This is a test report.";

        // 서비스 호출해서 Report 객체에 담기
        Report createdReport = reportService.createReport(member, 10L, content);

        // Report 객체가 정상적으로 생성되었는지 확인
        assertEquals(content, createdReport.getContent());
        assertEquals(member, createdReport.getMember());
        assertEquals(drawing, createdReport.getDrawing());

        // reportRepository의 save 메서드가 1번 호출되었는지 확인
        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    public void testCreateReport_DrawingNotFound() {
        // DrawingRepository의 findById 메서드를 Mock으로 대체하여 빈 Optional 객체 반환하도록 설정
        when(drawingRepository.findById(anyLong())).thenReturn(Optional.empty());

        String content = "This is a test report.";

        // 예외 발생 여부 확인
        assertThrows(BusinessException.class, () -> reportService.createReport(member, 10L, content));

        // reportRepository의 save 메서드가 호출되지 않았는지 확인
        verify(reportRepository, never()).save(any(Report.class));
    }
}