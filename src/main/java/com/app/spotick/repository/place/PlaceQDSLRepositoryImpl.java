package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.placeFile;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class PlaceQDSLRepositoryImpl implements PlaceQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceListDto> findPlaceListPaging(Pageable pageable, Long userId) {
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

        List<PlaceListDto> placeListDtos = queryFactory.select(place)
                .from(place)
                .innerJoin(placeFile)
                .on(place.id.eq(placeFile.place.id))
                .where(place.placeStatus.eq(PostStatus.APPROVED))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(GroupBy.groupBy(place.id)
                        .list(Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                list(
                                        Projections.constructor(PlaceFileDto.class,
                                                placeFile.id,
                                                placeFile.fileName,
                                                placeFile.uuid,
                                                placeFile.uploadPath
                                        )
                                ),
                                reviewAvg,
                                reviewCount,
                                bookmarkCount,
                                isBookmarkChecked
                        ))
                );

        placeListDtos.forEach(dto -> {
            dto.getPlaceAddress().cutAddress();
//            dto.updateEvalAvg(Optional.ofNullable(dto.getEvalAvg()).orElse(0.0));
            dto.cutPlaceFilesForListPage();
        });

        return placeListDtos;
    }
}






















