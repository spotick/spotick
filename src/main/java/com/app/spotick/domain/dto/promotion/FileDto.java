package com.app.spotick.domain.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDto {
    private String fileName;
    private String uuid;
    private String uploadPath;

    @Builder
    public FileDto(String fileName, String uuid, String uploadPath) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
    }
}
