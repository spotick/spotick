package com.app.spotick.repository.promotion.like;

import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.promotion.PromotionLike;
import com.app.spotick.domain.entity.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PromotionLikeRepository extends JpaRepository<PromotionLike, Long> {

    @Modifying
    @Query("delete from PromotionLike p where p.promotionBoard = :promotion and p.user = :user")
    void deleteLikeByPromotionAndUser(@Param("promotion") PromotionBoard promotion, @Param("user") User user);

}
