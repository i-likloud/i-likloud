package com.backend.api.drawing.service;

//import com.amazonaws.services.s3.AmazonS3Client;
import com.backend.api.drawing.dto.DrawingUploadDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.drawing.repository.DrawingFileRepository;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.service.PhotoService;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
//import com.backend.global.storage.S3Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrawingUploadService {

    private final DrawingRepository drawingRepository;

    private final DrawingFileRepository drawingfileRepository;

    private final PhotoService photoService;

    private final MemberService memberService;

//    private final AmazonS3Client amazonS3Client;

//    private final S3Storage s3Storage;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final Path fileStorageLocation = Paths.get("upload-drawings");


    // 파일 업로드 후 그림 게시물 생성
    @Transactional
    public DrawingUploadDto uploadFileAndCreateDrawings(MultipartFile file, String title, String content, MemberInfoDto memberInfoDto, Long photoId) {

        try {
            // MemberInfoDto에서 멤버 정보 가져옴
            log.info(memberInfoDto.getEmail());
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            DrawingFile drawingFile = saveDrawingFile(file);
            // TODO 사진 참조
            Drawing drawing = createDrawing(title, content, member, drawingFile, photoId);

            return new DrawingUploadDto(drawing);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_CONFLICT);
        }
    }
    // TODO S3 적용
//    @Transactional
//    public DrawingUploadDto saveFileAndCreateDrawings(@RequestParam("file") MultipartFile file, Drawing drawingRequest, MemberInfoDto memberInfoDto, Long photoId) {
//        try {
//            // MemberInfoDto에서 멤버 정보 가져옴
//            log.info(memberInfoDto.getEmail());
//            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
//            DrawingFile drawingFile = saveDrawingFile(file);
//            Drawing drawing = createDrawing(drawingRequest, member, drawingFile, photoId);
//
//            return new DrawingUploadDto(drawing);
//
//        } catch (IOException e) {
//            throw new BusinessException(ErrorCode.FILE_UPLOAD_CONFLICT);
//        }
//    }

    // 그림파일 저장
    @Transactional
    public DrawingFile saveDrawingFile(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        // 파일 이름이 올바른지 확인
        if(originalFilename.contains("..")) {
            throw new BusinessException(ErrorCode.INVALID_FILE_NAME);
        }
        // DrawingFile 저장 및 id 값 획득
        DrawingFile drawingFile = new DrawingFile(originalFilename, null);
        drawingfileRepository.save(drawingFile);

        // 중복 방지 위해 그림 이름앞에 id 붙이기
        Long fileId = drawingFile.getDrawingFileId();
        String filename = fileId + "_" + originalFilename;
        // S3에 파일을 저장
//        s3Storage.saveFileS3(file, filename);
//        // 파일의 URL을 생성
//        String fileUrl = amazonS3Client.getUrl(bucket, filename).toString();
//        updateDrawingFileWithUrl(drawingFile, fileUrl);
//
//        return drawingFile;
        // 로컬에 파일 저장
        Path targetLocation = this.fileStorageLocation.resolve(filename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        // 파일의 URL을 생성
        String fileUrl = "/upload-drawings/" + filename;
        drawingFile.setFilePath(fileUrl);
        drawingfileRepository.save(drawingFile);

        return drawingFile;
    }

    // 그림파일 Url 업데이트
    private void updateDrawingFileWithUrl(DrawingFile drawingFile, String fileUrl) {
        drawingFile.setFilePath(fileUrl);
        drawingfileRepository.save(drawingFile);
    }

    // 그림 게시물 생성
    @Transactional
    public Drawing createDrawing(String title, String content, Member member, DrawingFile drawingFile, Long photoId) {
        Photo findPhoto = photoService.findPhotoByPhotoId(photoId);

        Drawing drawing = Drawing.builder()
                .title(title)
                .artist(member.getNickname())
                .content(content)
                .imageUrl(drawingFile.getFilePath())
                .viewCount(0)
                .likesCount(0)
                .drawingFile(drawingFile)
                .member(member)
                .photo(findPhoto)
                .build();

        drawingFile.setDrawing(drawing);
        drawingRepository.save(drawing);

        return drawing;
    }



}
