package com.app.spotick.service.promotion.like;

import com.app.spotick.domain.entity.compositePk.PromotionLikeId;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.promotion.PromotionLike;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.promotion.PromotionRepository;
import com.app.spotick.repository.promotion.like.PromotionLikeRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PromotionLikeServiceImpl implements PromotionLikeService {
    private final PromotionRepository promotionRepository;
    private final PromotionLikeRepository promotionLikeRepository;
    private final UserRepository userRepository;

    @Override
    public void doLike(Long promotionId, Long userId) {
        PromotionBoard tmpPromotion = promotionRepository.getReferenceById(promotionId);
        User tmpUser = userRepository.getReferenceById(userId);

        PromotionLikeId promotionLikeId = new PromotionLikeId(promotionId, userId);

        PromotionLike entity = PromotionLike.builder()
                .id(promotionLikeId)
                .promotionBoard(tmpPromotion)
                .user(tmpUser)
                .build();

        promotionLikeRepository.save(entity);
    }

    @Override
    public void undoLike(Long promotionId, Long userId) {
        PromotionBoard tmpPromotion = promotionRepository.getReferenceById(promotionId);
        User tmpUser = userRepository.getReferenceById(userId);

        promotionLikeRepository.deleteLikeByPromotionAndUser(tmpPromotion, tmpUser);
    }
}
