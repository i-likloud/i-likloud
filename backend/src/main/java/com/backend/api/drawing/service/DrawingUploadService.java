package com.backend.api.drawing.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.backend.api.drawing.dto.DrawingUploadDto;
import com.backend.domain.drawing.entity.Drawing;
import com.backend.domain.drawing.entity.DrawingFile;
import com.backend.domain.drawing.repository.DrawingFileRepository;
import com.backend.domain.drawing.repository.DrawingRepository;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.repository.PhotoRepository;
import com.backend.domain.photo.service.PhotoService;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class DrawingUploadService {

    private final DrawingRepository drawingRepository;

    private final DrawingFileRepository drawingfileRepository;

    private final PhotoService photoService;

    private final PhotoRepository photoRepository;

    private final MemberService memberService;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // 파일 업로드 후 그림 게시물 생성
    @Transactional
    public DrawingUploadDto uploadFileAndCreateDrawings(MultipartFile file, String title, String content, MemberInfoDto memberInfoDto, Long photoId) {

        try {
            // MemberInfoDto에서 멤버 정보 가져옴
            log.info(memberInfoDto.getEmail());
            Member member = memberService.findMemberByEmail(memberInfoDto.getEmail());
            DrawingFile drawingFile = uploadDrawingFile(file, title);

            Drawing drawing = createDrawing(title, content, member, drawingFile, photoId);

            drawingFile.setDrawing(drawing);

            // 연결된 사진의 선택 횟수 증가
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PHOTO));

            photo.incrementPickCnt();

            return new DrawingUploadDto(drawing);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_CONFLICT);
        }
    }

    // 그림파일 저장
    @Transactional
    public DrawingFile uploadDrawingFile(MultipartFile file, String title) throws IOException {
        String folderName = "drawing/";
        String originalFileName = file.getOriginalFilename();
        String filename = folderName + UUID.randomUUID() + originalFileName;

        // S3에 파일을 저장
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());
        // 확장자에 따른 Content-Type 설정
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension.equalsIgnoreCase("jpg")) {
            objMeta.setContentType("image/jpg");
        } else if (extension.equalsIgnoreCase("png")) {
            objMeta.setContentType("image/png");
        } else {
            objMeta.setContentType("image/jpeg");
        }
        /**
         * Content-Disposition
         * inline : 웹에서 파일 조회
         * attachment : 다운로드
         */
        objMeta.setContentType(file.getContentType());
        objMeta.setHeader("Content-Disposition", "inline");
        InputStream newFile = file.getInputStream();
        amazonS3Client.putObject(bucket, filename, newFile, objMeta);
        String imageUrl = URLDecoder.decode(amazonS3Client.getUrl(bucket, filename).toString(), StandardCharsets.UTF_8);

        // DrawingFile 저장 및 id 값 획득
        DrawingFile drawingFile = DrawingFile.builder()
                        .fileName(title)
                        .filePath(imageUrl)
                        .build();

        drawingfileRepository.save(drawingFile);

        // 로컬에 파일 저장
//        Path targetLocation = this.fileStorageLocation.resolve(filename);
//
//        try (InputStream inputStream = file.getInputStream()) {
//            Files.copy(inputStream, targetLocation,
//                    StandardCopyOption.REPLACE_EXISTING);
//        }
//        // 파일의 URL을 생성
//        String fileUrl = "/upload-drawings/" + filename;
//        drawingFile.setFilePath(fileUrl);
//        drawingfileRepository.save(drawingFile);

        return drawingFile;
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

        drawingRepository.save(drawing);
        return drawing;
    }

}
