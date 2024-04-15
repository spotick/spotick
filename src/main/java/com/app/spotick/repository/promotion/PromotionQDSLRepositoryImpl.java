package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.app.spotick.domain.entity.promotion.QPromotionBoard.*;
import static com.app.spotick.domain.entity.promotion.QPromotionLike.*;
import static com.app.spotick.domain.entity.user.QUser.*;
import static com.app.spotick.domain.entity.user.QUserProfileFile.*;

@RequiredArgsConstructor
public class PromotionQDSLRepositoryImpl implements PromotionQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PromotionDetailDto> findPromotionById(Long promotionId, Long userId) {

        return Optional.ofNullable(queryFactory
                .from(promotionBoard)
                .where(
                        promotionBoard.id.eq(promotionId)
                )
                .join(user.userProfileFile, userProfileFile)
                .select(Projections.constructor(PromotionDetailDto.class,
                        promotionBoard.id,
                        user.id,
                        user.nickName,
                        Projections.constructor(FileDto.class,
                                userProfileFile.fileName,
                                userProfileFile.uuid,
                                userProfileFile.uploadPath
                        ),
                        promotionBoard.createdDate,
                        promotionBoard.title,
                        promotionBoard.subTitle,
                        promotionBoard.content,
                        Projections.constructor(FileDto.class,
                                promotionBoard.fileName,
                                promotionBoard.uuid,
                                promotionBoard.uploadPath
                        ),
                        likeCount(),
                        isLiked(userId)
                ))
                .fetchOne());
    }

    private JPQLQuery<Long> likeCount() {
        return JPAExpressions
                .select(promotionLike.count())
                .from(promotionLike)
                .where(promotionLike.promotionBoard.eq(promotionBoard));
    }

    private BooleanExpression isLiked(Long userId) {
        return userId == null ?
                Expressions.asBoolean(false)
                : JPAExpressions.select(promotionLike.id.isNotNull())
                .from(promotionLike)
                .where(promotionLike.promotionBoard.eq(promotionBoard).and(promotionLike.user.id.eq(userId)))
                .exists();
    }
}
