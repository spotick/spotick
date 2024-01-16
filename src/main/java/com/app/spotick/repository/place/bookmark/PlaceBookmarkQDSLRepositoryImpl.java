package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.app.spotick.domain.entity.place.QPlace.*;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.*;
import static com.app.spotick.domain.entity.place.QPlaceFile.*;
import static com.app.spotick.domain.entity.place.QPlaceReview.*;

@RequiredArgsConstructor
public class PlaceBookmarkQDSLRepositoryImpl implements PlaceBookmarkQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceListDto> findBookmarkedPlacesByUserId(Long userId) {
        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        return queryFactory.select(
                        Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
//                                게시글의 사진 리스트를 도대체 어떤 방식으로 가져와야 하는가?

                                reviewAvg,
                                reviewCount,
                                bookmarkCount
                        )
                )
                .from(placeBookmark)
                .join(placeBookmark.place, place)
                .leftJoin(placeFile.place, place)
                .where(placeBookmark.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED))
                .fetch();
    }
}


