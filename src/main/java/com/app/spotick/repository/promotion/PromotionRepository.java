package com.app.spotick.repository.promotion;

import com.app.spotick.domain.entity.promotion.PromotionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionBoard, Long>, PromotionQDSLRepository {
}