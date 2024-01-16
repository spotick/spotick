package com.app.spotick.domain.dto.place;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PlaceFileDto {
    private String fileName;
    private String uuid;
    private String uploadPath;

    public PlaceFileDto(String fileName, String uuid, String uploadPath) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
    }
}
