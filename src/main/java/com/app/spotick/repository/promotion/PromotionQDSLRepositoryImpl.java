package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
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
                        promotionBoard.promotionCategory,
                        likeCount(),
                        isLiked(userId)
                ))
                .fetchOne());
    }

    @Override
    public Slice<PromotionListDto> findPromotionList(Pageable pageable,
                                                     PromotionCategory category) {
        BooleanExpression categoryCondition = null;
        BooleanExpression userCondition = null;

        if (category != null) {
            categoryCondition = promotionBoard.promotionCategory.eq(category);
        }

        List<PromotionListDto> contents = queryFactory
                .select(Projections.constructor(PromotionListDto.class,
                        promotionBoard.id,
                        promotionBoard.title,
                        Projections.constructor(FileDto.class,
                                promotionBoard.fileName,
                                promotionBoard.uuid,
                                promotionBoard.uploadPath
                        )
                ))
                .from(promotionBoard)
                .where(
                        categoryCondition,
                        userCondition
                )
                .orderBy(promotionBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<PromotionListDto> findPromotionListOfUser(Pageable pageable, Long writerId, Long promotionId) {
        List<PromotionListDto> contents = queryFactory
                .select(Projections.constructor(PromotionListDto.class,
                        promotionBoard.id,
                        promotionBoard.title,
                        Projections.constructor(FileDto.class,
                                promotionBoard.fileName,
                                promotionBoard.uuid,
                                promotionBoard.uploadPath
                        )
                ))
                .from(promotionBoard)
                .where(
                        promotionBoard.user.id.eq(writerId),
                        promotionBoard.id.ne(promotionId)
                )
                .orderBy(promotionBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
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
