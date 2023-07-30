package com.backend.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.backend.domain.member.entity.Member;
import com.backend.domain.photo.dto.PhotoUploadDto;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.entity.PhotoFile;
import com.backend.domain.photo.repository.PhotoFileRepository;
import com.backend.domain.photo.repository.PhotoRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoFileRepository photoFileRepository;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public PhotoUploadDto saveFileAndCreatePhotos(MultipartFile file, Member member) {

        try {
            PhotoFile photoFile = uploadPhotoFile(file, "cloud");

            Photo photo = createPhoto(photoFile, member);

            photoFile.setPhoto(photo);

            return new PhotoUploadDto(photo, member.getMemberId(), photoFile.getPhotoFileId());

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_CONFLICT);
        }
    }

    // 사진 파일 저장
    @Transactional
    public PhotoFile uploadPhotoFile(MultipartFile file, String photoType) throws IOException {
        // 사진 상태에 따라 폴더 나눔
        String folderName;
        if (photoType.equals("cloud")){
            folderName = "photo/";
        } else if (photoType.equals("noCloud")){
            folderName = "no-cloud/";
        } else{
            folderName = "danger/";
        }
        String photoFilename = folderName + UUID.randomUUID() + file.getOriginalFilename();

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
        objMeta.setHeader("Content-Disposition", "inline");
        InputStream newFile = file.getInputStream();
        amazonS3Client.putObject(bucket, photoFilename, newFile, objMeta);
        String imageUrl = URLDecoder.decode(amazonS3Client.getUrl(bucket, photoFilename).toString(), StandardCharsets.UTF_8);

        // PhotoFile 저장 및 id 값 획득
        PhotoFile photoFile = PhotoFile.builder()
                .fileName(photoFilename)
                .filePath(imageUrl)
                .build();

        photoFileRepository.save(photoFile);
        return photoFile;
    }

    // 사진 게시물 생성
    @Transactional
    public Photo createPhoto(PhotoFile photoFile, Member member) {
        Photo photo = Photo.builder()
                .member(member)
                .photoUrl(photoFile.getFilePath())
                .pickCnt(0)
                .bookmarkCnt(0)
                .photoFile(photoFile)
                .build();

        photoRepository.save(photo);
        return photo;

    }

    public Photo findPhotoByPhotoId(Long photoId){
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_FILE));
    }
}
