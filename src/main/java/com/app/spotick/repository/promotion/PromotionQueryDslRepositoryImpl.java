package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.entity.promotion.PromotionFile;
import com.app.spotick.domain.entity.promotion.QPromotionBoard;
import com.app.spotick.domain.entity.promotion.QPromotionFile;
import com.app.spotick.domain.entity.promotion.QPromotionLike;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.promotion.QPromotionBoard.promotionBoard;
import static com.app.spotick.domain.entity.promotion.QPromotionFile.promotionFile;
import static com.app.spotick.domain.entity.promotion.QPromotionLike.promotionLike;
@RequiredArgsConstructor
public class PromotionQueryDslRepositoryImpl implements PromotionQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PromotionDetailDto> findPromotionDetailById(Long promotionId,Long userId) {
        JPQLQuery<Long> likeCount = JPAExpressions.select(promotionLike.count())
                .from(promotionLike)
                .where(promotionLike.promotionBoard.eq(promotionBoard));

        JPQLQuery<PromotionFile> mainImgFile = JPAExpressions.selectFrom(promotionFile)
                .where(promotionBoard.id.eq(promotionFile.promotionBoard.id));

        BooleanExpression likeChecked = userId == null ?
                Expressions.asBoolean(false)
                : JPAExpressions.select(promotionLike.id.isNotNull())
                .from(promotionLike)
                .where(promotionLike.promotionBoard.eq(promotionBoard).and(promotionLike.user.id.eq(userId)))
                .exists();


        return Optional.ofNullable(queryFactory.select(
                        Projections.constructor(PromotionDetailDto.class,
                                promotionBoard.id,
                                promotionBoard.user.id,
                                promotionBoard.title,
                                promotionBoard.subTitle,
                                promotionBoard.promotionAddress,
                                mainImgFile,
                                likeCount,
                                likeChecked
                        )
                )
                .from(promotionBoard)
                .where(promotionBoard.id.eq(promotionId))
                .fetchOne());
    }
}
