package com.app.spotick.repository.promotion;

import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionBoard, Long>, PromotionQDSLRepository {
    Optional<PromotionBoard> findByIdAndUser(Long id, User user);
}