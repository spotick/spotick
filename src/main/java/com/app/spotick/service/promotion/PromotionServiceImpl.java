package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.promotion.PromotionRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.place.file.PlaceFileService;
import com.app.spotick.service.promotion.file.PromotionFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final PromotionFileService promotionFileService;
    @Override
    public void registerPromotion(PromotionRegisterDto promotionRegisterDto, Long userId) throws IOException {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원"));
        PromotionBoard promotionBoard = promotionRegisterDto.toEntity();
        promotionBoard.setWriter(writer);

        promotionBoard = promotionRepository.save(promotionBoard);
//        사진파일 넣기
        List<MultipartFile> promotionFiles = new ArrayList<>();
//        인덱스 0번에 메인사진
        promotionFiles.add(promotionRegisterDto.getPromotionMainFile());
//        나머지 게시글에 들어간 사진들
        promotionFiles.addAll(promotionRegisterDto.getPromotionFiles());


        promotionFileService.registerAndSavePromotionFile(promotionFiles,promotionBoard);
    }
}
