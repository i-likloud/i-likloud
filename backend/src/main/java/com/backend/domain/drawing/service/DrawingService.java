package com.backend.domain.drawing.service;

import com.backend.domain.drawing.dto.DrawingResponseDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.drawing.repository.DrawingFileRepository;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DrawingService {

    @Autowired
    private DrawingRepository drawingRepository;

    @Autowired
    private DrawingFileRepository drawingfileRepository;

    @Autowired
    private MemberService memberService;

    private final Path fileStorageLocation = Paths.get("upload-drawings");

    public DrawingResponseDto saveFileAndCreateDrawings(MultipartFile file, Drawing drawingRequest, MemberInfoDto memberInfoDto) {

        // MemberInfoDto에서 멤버 정보 가져옴
        log.info(memberInfoDto.getEmail());
        Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 파일 이름이 올바른지 확인
            if(originalFilename.contains("..")) {
                throw new BusinessException(ErrorCode.INVALID_FILE_NAME);
            }
            // DrawingFile 저장 및 id 값 획득
            DrawingFile drawingFile = new DrawingFile(originalFilename, null);
            drawingFile = drawingfileRepository.save(drawingFile);
            Long fileId = drawingFile.getDrawingFileId();

            String filename = fileId + "_" + originalFilename;

            Path targetLocation = this.fileStorageLocation.resolve(filename);

            // 파일 저장
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            // 파일의 URL을 생성
            String fileUrl = "/upload-drawings/" + filename;
            drawingFile.setFilePath(fileUrl);
            drawingfileRepository.save(drawingFile);

            Drawing drawing = Drawing.builder()
                    .title(drawingRequest.getTitle())
                    .content(drawingRequest.getContent())
                    .drawingFile(drawingFile)
                    .imageUrl(fileUrl)
                    .member(member)
                    .build();

            drawing.setArtist(member.getNickname());
            drawingFile.setDrawing(drawing);
            drawingRepository.save(drawing);

            return new DrawingResponseDto(drawing);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_CONFLICT);
        }
    }

    public List<DrawingResponseDto> getAllDrawings(){
        List<Drawing> drawings = drawingRepository.findAll();
        return drawings.stream()
                .map(DrawingResponseDto::new)
                .collect(Collectors.toList());
    }

}
