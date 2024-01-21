package com.app.spotick.domain.dto.place;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PlaceFileDto {
    private Long placeFileId;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private Long placeId;

    public PlaceFileDto(Long placeFileId,String fileName, String uuid, String uploadPath,Long placeId) {
        this.placeFileId = placeFileId;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.placeId = placeId;
    }
}
