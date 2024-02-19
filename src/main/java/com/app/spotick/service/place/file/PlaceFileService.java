package com.app.spotick.service.place.file;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlaceFileService {
    void registerAndSavePlaceFile(List<MultipartFile> placeFiles, Place place) throws IOException;

}
