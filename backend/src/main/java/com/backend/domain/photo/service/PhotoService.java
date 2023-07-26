package com.backend.domain.photo.service;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.service.MemberService;
import com.backend.domain.photo.dto.PhotoResponseDto;
import com.backend.domain.photo.entity.Photo;
import com.backend.domain.photo.entity.PhotoFile;
import com.backend.domain.photo.repository.PhotoFileRepository;
import com.backend.domain.photo.repository.PhotoRepository;
import com.backend.global.error.ErrorCode;
import com.backend.global.error.exception.BusinessException;
import com.backend.global.resolver.memberInfo.MemberInfoDto;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoFileRepository photoFileRepository;

    @Autowired
    private MemberService memberService;

    private final Path fileStorageLocation = Paths.get("upload-photos");

    public PhotoResponseDto saveFileAndCreatePhotos(MultipartFile file, Photo photoRequest, MemberInfoDto memberInfoDto) {

        log.info(memberInfoDto.getEmail());
        Member member = memberService.findMemberByEmail((memberInfoDto.getEmail()));

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 파일 이름이 올바른지 확인
            if (originalFilename.contains("..")) {
                throw new BusinessException(ErrorCode.INVALID_FILE_NAME);
            }
            // PhotoFile 저장 및 id 값 획득
            PhotoFile photoFile = new PhotoFile(originalFilename, null);
            photoFile = photoFileRepository.save(photoFile);
            Long fileId = photoFile.getPhotoFileId();

            String filename = fileId + "_" + originalFilename;

            Path targetLocation = this.fileStorageLocation.resolve(filename);

            // 파일 저장
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            //파일의 URL을 생성
            String fileUrl = "/upload-photos/" + filename;
            photoFile.setFilePath(fileUrl);
            photoFileRepository.save(photoFile);

//            this.photoId = photoId;
//            this.member = member;
//            this.photoUrl = photoUrl;
//            this.pickCnt = pickCnt;
//            this.drawings = drawings;
//            this.photoFile = photoFile;
            Photo photo = Photo.builder()
                    .member(member)
                    .photoUrl(fileUrl)
                    .photoFile(photoFile)
                    .pickCnt(photoRequest.getPickCnt())
                    .build();

            photoFile.setPhoto(photo);
            photoRepository.save(photo);

            return new PhotoResponseDto(photo);

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_CONFLICT);
        }
    }
    
}
