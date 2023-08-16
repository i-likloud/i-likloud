package com.backend.domain.drawing.entity;

import com.backend.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawingFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawingFileId;

    @Column(nullable = false)
    private String fileName;

    private String filePath;

    @OneToOne(mappedBy = "drawingFile", fetch = FetchType.LAZY)
    private Drawing drawing;

    public DrawingFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
