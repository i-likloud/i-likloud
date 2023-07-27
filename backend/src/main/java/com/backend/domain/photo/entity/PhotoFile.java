package com.backend.domain.photo.entity;

import com.backend.domain.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoFileId;

    @Column(nullable = false)
    private String fileName;

    private String filePath;

    @OneToOne(mappedBy = "photoFile")
    private Photo photo;

    public PhotoFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
