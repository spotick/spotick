package com.app.spotick.service.promotion.file;

import com.app.spotick.domain.entity.promotion.PromotionBoard;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PromotionFileService {
    void registerAndSavePromotionFile(MultipartFile promotionFile, PromotionBoard promotionBoard) throws IOException;

}
