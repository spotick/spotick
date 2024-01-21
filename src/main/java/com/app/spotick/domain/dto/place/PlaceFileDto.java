package com.app.spotick.domain.dto.place;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PlaceFileDto {
    private Long placeFileId;
    private String fileName;
    private String uuid;
    private String uploadPath;

    public PlaceFileDto(Long placeFileId,String fileName, String uuid, String uploadPath) {
        this.placeFileId = placeFileId;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
    }
}
