package com.app.spotick.service.promotion.file;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PromotionFileService {
    void registerAndSavePromotionFile(List<MultipartFile> promotionFiles, PromotionBoard promotionBoard) throws IOException;

}
