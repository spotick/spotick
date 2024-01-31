package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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
        log.info("보드 만들어짐");
//        사진파일 넣기
        MultipartFile promotionFile = promotionRegisterDto.getPlaceFile();


        log.info("사진 업로드 전");
        promotionFileService.registerAndSavePromotionFile(promotionFile,promotionBoard);
        log.info("사진 업로드 후");
    }

    @Override
    public void removePromotion(Long promotionId) {

    }

    @Override
    public PromotionDetailDto promotionDetail(Long promotionId) {
        return null;
    }

    @Override
    public Page<PromotionListDto> findPromotionList(Pageable pageable) {
        return promotionRepository.findListWithPage(pageable);
    }
}
