package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.dto.place.reservation.ReservationRequestListDto;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.app.spotick.domain.entity.place.QPlace.*;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.*;
import static com.app.spotick.domain.entity.place.QPlaceReservation.*;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;
import static com.app.spotick.domain.entity.user.QUser.*;
import static com.app.spotick.domain.entity.user.QUserProfileFile.*;

@RequiredArgsConstructor
public class PlaceReservationQDSLRepositoryImpl implements PlaceReservationQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PlaceReservationListDto> findReservationsByUserId(Long userId, Pageable pageable) {
        // 게시글 전체 갯수 확인
        JPAQuery<Long> totalCountQuery = queryFactory.select(placeReservation.count())
                .from(placeReservation)
                .join(placeReservation.place, place)
                .where(placeReservation.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED));

        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.placeReservation.place.eq(place));

        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.placeReservation.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        List<PlaceReservationListDto> placeReservationListDtos = queryFactory
                .from(placeReservation)
                .join(placeReservation.place, place)
                .leftJoin(place.placeFileList, placeFile)
                .where(placeReservation.user.id.eq(userId),
                        placeFile.id.eq(
                                JPAExpressions.select(placeFile.id.min())
                                        .from(placeFile)
                                        .where(placeFile.place.id.eq(place.id))
                        )
                )
                .orderBy(placeReservation.id.desc())
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
                                        placeFile.uploadPath,
                                        place.id
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

        return PageableExecutionUtils.getPage(placeReservationListDtos, pageable, totalCountQuery::fetchOne);
    }

    @Override
    public Slice<ReservationRequestListDto> findReservationsByPlaceIdAndUserIdSlice(Long placeId, Long userId, Pageable pageable) {

        List<ReservationRequestListDto> contents = queryFactory
                .from(placeReservation)
                .join(placeReservation.user, user)
                .join(user.userProfileFile, userProfileFile)
                .where(
                        placeReservation.place.id.eq(placeId),
                        placeReservation.place.user.id.eq(userId),
                        placeReservation.reservationStatus.eq(PlaceReservationStatus.PENDING)
                )
                .orderBy(placeReservation.checkIn.asc(), placeReservation.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .select(
                        Projections.constructor(ReservationRequestListDto.class,
                                placeReservation.id,
                                placeReservation.visitors,
                                placeReservation.checkIn,
                                placeReservation.checkOut,
                                placeReservation.amount,
                                placeReservation.content,
                                user.nickName,
                                userProfileFile.fileName,
                                userProfileFile.uuid,
                                userProfileFile.uploadPath,
                                userProfileFile.isDefaultImage
                        )
                )
                .fetch();

        boolean hasNext = false;

        if(contents.size() > pageable.getPageSize()){
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

}









