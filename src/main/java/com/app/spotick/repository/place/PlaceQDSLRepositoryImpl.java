package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.placeFile;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;

@RequiredArgsConstructor
public class PlaceQDSLRepositoryImpl implements PlaceQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PlaceListDto> findPlaceListPaging(Pageable pageable, Long userId) {
        System.out.println("============================================");
        System.out.println(userId);
        System.out.println("============================================");
        List<PlaceListDto> content = getPlaceListDTOs(pageable, userId);
        Long count = getPlaceListDTOsCount();
        return new PageImpl<>(content, pageable, count);
    }

    private List<PlaceListDto> getPlaceListDTOs(Pageable pageable, Long userId) {
        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        //        로그인 되어있지 않으면 쿼리 실행 x
        BooleanExpression isBookmarkChecked = userId == null ?
                Expressions.asBoolean(false)
                : JPAExpressions.select(placeBookmark.id.isNotNull())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place).and(placeBookmark.user.id.eq(userId)))
                .exists();

        List<PlaceListDto> placeListDtos = queryFactory.select(
                        Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                reviewAvg,
                                reviewCount,
                                bookmarkCount,
                                isBookmarkChecked
                        )
                )
                .from(place)
                .where(place.placeStatus.eq(PostStatus.APPROVED))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())   //페이지 번호
                .limit(pageable.getPageSize())  //페이지 사이즈
                .fetch();

        placeListDtos.forEach(placeListDto -> {
            placeListDto.updateEvalAvg(Optional.ofNullable(placeListDto.getEvalAvg()).orElse(0.0));

            placeListDto.getPlaceAddress().cutAddress();

            placeListDto.updatePlaceFiles(queryFactory.select(
                            Projections.constructor(PlaceFileDto.class,
                                    placeFile.id,
                                    placeFile.fileName,
                                    placeFile.uuid,
                                    placeFile.uploadPath
                            )
                    )
                    .from(placeFile)
                    .where(placeFile.place.id.eq(placeListDto.getId()))
                    .limit(5)
                    .fetch());
        });

        return placeListDtos;
    }

    private Long getPlaceListDTOsCount() {
        return queryFactory.select(place.count())
                .from(place)
                .where(place.placeStatus.eq(PostStatus.APPROVED))
                .fetchOne();
    }


}






















