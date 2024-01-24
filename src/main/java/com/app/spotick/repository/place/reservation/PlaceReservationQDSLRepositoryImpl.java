package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.QPlace;
import com.app.spotick.domain.entity.place.QPlaceFile;
import com.app.spotick.domain.entity.place.QPlaceReservation;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.app.spotick.domain.entity.place.QPlace.*;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.*;
import static com.app.spotick.domain.entity.place.QPlaceReservation.*;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;

@RequiredArgsConstructor
public class PlaceReservationQDSLRepositoryImpl implements PlaceReservationQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PlaceReservationListDto> findReservationsByUserId(Long userId, Pageable pageable) {
        // 게시글 전체 갯수 확인
        JPAQuery<Long> totalCountQuery = queryFactory.select(place.count())
                .from(placeReservation)
                .join(placeReservation.place, place)
                .where(placeReservation.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED));

        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        List<PlaceReservationListDto> placeReservationListDtos = queryFactory
                .from(placeReservation)
                .join(placeReservation.place, place)
                .leftJoin(place.placeFileList, placeFile)
                .where(placeReservation.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED),
                        placeFile.id.eq(
                                JPAExpressions.select(placeFile.id.min())
                                        .from(placeFile)
                                        .where(placeFile.place.id.eq(place.id))
                        )
                )
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .select(
                        Projections.constructor(PlaceReservationListDto.class,
                                place.id,
                                placeReservation.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                Projections.constructor(PlaceFileDto.class,
                                        placeFile.id,
                                        placeFile.fileName,
                                        placeFile.uuid,
                                        placeFile.uploadPath
                                ),
                                reviewAvg,
                                reviewCount,
                                bookmarkCount,
                                placeReservation.visitors,
                                placeReservation.checkIn,
                                placeReservation.checkOut,
                                placeReservation.content,
                                placeReservation.reservationStatus
                        )
                )
                .fetch();

        placeReservationListDtos.forEach(dto -> {
            dto.getPlaceAddress().cutAddress();
        });

//        return placeReservationListDtos;
        return PageableExecutionUtils.getPage(placeReservationListDtos, pageable, totalCountQuery::fetchOne);
    }
}
