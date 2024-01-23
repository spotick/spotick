package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.entity.place.PlaceFile;
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

    public static PlaceFileDto from(PlaceFile placeFile){
        PlaceFileDto placeFileDto = new PlaceFileDto();
        placeFileDto.setPlaceFileId(placeFile.getId());
        placeFileDto.setFileName(placeFile.getFileName());
        placeFileDto.setUuid(placeFile.getUuid());
        placeFileDto.setUploadPath(placeFile.getUploadPath());

        return placeFileDto;
    }
}
