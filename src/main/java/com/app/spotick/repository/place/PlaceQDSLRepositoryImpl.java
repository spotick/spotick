package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceDetailDto;
import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveBasicInfoDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReservedNotReviewedDto;
import com.app.spotick.domain.entity.place.*;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.placeFile;
import static com.app.spotick.domain.entity.place.QPlaceInquiry.placeInquiry;
import static com.app.spotick.domain.entity.place.QPlaceReservation.*;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class PlaceQDSLRepositoryImpl implements PlaceQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceListDto> findPlaceListPaging(Pageable pageable, Long userId) {

        JPQLQuery<Double> reviewAvg = createReviewAvgSub(place);

        JPQLQuery<Long> reviewCount = createReviewCountSub(place);

        JPQLQuery<Long> bookmarkCount = createBookmarkCountSub(place);

        BooleanExpression isBookmarkChecked = isBookmarkCheckedSub(place, userId);

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

//        포스트의 id만 list로 가져온다
        List<Long> placeIdList = placeListDtos.stream().map(PlaceListDto::getId).toList();

//        가져온 id리스트를 in절의 조건으로 사진정보들을 가져온다.
        List<PlaceFileDto> fileDtoList = queryFactory.select(
                        Projections.constructor(PlaceFileDto.class,
                                placeFile.id,
                                placeFile.fileName,
                                placeFile.uuid,
                                placeFile.uploadPath,
                                placeFile.place.id
                        ))
                .from(placeFile)
                .where(placeFile.place.id.in(placeIdList))
                .orderBy(placeFile.id.asc(), placeFile.place.id.desc())
                .fetch();

//        사진정보를 장소 id별로 묶는다
        Map<Long, List<PlaceFileDto>> fileListMap = fileDtoList.stream().collect(Collectors.groupingBy(PlaceFileDto::getPlaceId));

//        장소 id별로 구분된 사진들을 각각 게시글 번호에 맞게 추가한다
        placeListDtos.forEach(placeListDto -> {
            placeListDto.updatePlaceFiles(fileListMap.get(placeListDto.getId())
                    .stream().limit(5L).toList());
            // 화면에서 뿌릴 주소값 가공
            placeListDto.getPlaceAddress().cutAddress();
        });

        return placeListDtos;
    }

    @Override
    public Optional<PlaceDetailDto> findPlaceDetailById(Long placeId, Long userId) {
        JPQLQuery<Double> reviewAvgSub = createReviewAvgSub(place);
        JPQLQuery<Long> reviewCountSub = createReviewCountSub(place);
        JPQLQuery<Long> inquiryCountSub = createInquiryCountSub(place);
        BooleanExpression bookmarkCheckedSub = isBookmarkCheckedSub(place, userId);
        QPlaceReview subReview = new QPlaceReview("pr");
        JPQLQuery<Long> review5scoreCount = JPAExpressions.select(subReview.id.count())
                .from(subReview)
                .where(subReview.placeReservation.place.id.eq(placeId), subReview.score.eq(5));

        List<Tuple> tupleList = queryFactory.select(
                        place,
                        reviewAvgSub,
                        reviewCountSub,
                        inquiryCountSub,
                        bookmarkCheckedSub,
                        review5scoreCount
                ).from(place)
                .join(place.placeFileList).fetchJoin()
                .where(place.id.eq(placeId))
                .fetch();
        PlaceDetailDto placeDetailDto = tupleList.stream().map(tuple -> {
            Place foundPlace = tuple.get(place);
            PlaceDetailDto placeDetail = PlaceDetailDto.from(foundPlace);
            placeDetail.setPlaceFileList(foundPlace.getPlaceFileList().stream().map(PlaceFileDto::from).toList());
            placeDetail.setEvalAvg(tuple.get(reviewAvgSub)==null?0.0:tuple.get(reviewAvgSub));
            placeDetail.setEvalCount(tuple.get(reviewCountSub));
            placeDetail.setInquiryCount(tuple.get(inquiryCountSub));
            placeDetail.setBookmarkChecked(tuple.get(bookmarkCheckedSub));
            placeDetail.setEval5ScoreCount(tuple.get(review5scoreCount));
            return placeDetail;
        }).findFirst().orElseThrow(() -> new IllegalStateException("잘못된 게시글"));

        return Optional.ofNullable(placeDetailDto);
    }

    @Override
    public Optional<PlaceReserveBasicInfoDto> findPlaceReserveBasicInfo(Long placeId) {
        QPlaceFile subFile = new QPlaceFile("subFile");
        PlaceReserveBasicInfoDto placeReserveBasicInfoDto = queryFactory.select(
                        Projections.constructor(PlaceReserveBasicInfoDto.class,
                                place.id,
                                place.title,
                                place.subTitle,
                                place.defaultPeople,
                                place.price,
                                place.surcharge,
                                Projections.constructor(PlaceFileDto.class,
                                        placeFile.id,
                                        placeFile.fileName,
                                        placeFile.uuid,
                                        placeFile.uploadPath,
                                        placeFile.place.id
                                )
                        )
                ).from(place)
                .join(place.placeFileList, placeFile)
                .where(place.id.eq(placeId).and(placeFile.id.eq(
                        JPAExpressions.select(subFile.id.min())
                                .from(subFile)
                                .where(subFile.place.id.eq(placeId)))
                ))
                .fetchOne();

        return Optional.ofNullable(placeReserveBasicInfoDto);
    }

    @Override
    public Page<PlaceReservedNotReviewedDto> findPlaceListNotRelatedToReview(Long userId, Pageable pageable) {
        JPAQuery<Long> totalCountQuery = queryFactory.select(placeReservation.count())
                .from(placeReservation)
                .join(placeReservation.place, place)
                .leftJoin(placeReservation.placeReview, placeReview)
                .where(placeReservation.user.id.eq(userId),
                        place.placeStatus.eq(PostStatus.APPROVED),
                        placeReservation.reservationStatus.eq(PlaceReservationStatus.COMPLETED),
                        placeReservation.checkOut.lt(LocalDateTime.now()),
                        placeReservation.notReviewing.eq(false),
                        placeReview.isNull());

        JPQLQuery<Double> reviewAvg = createReviewAvgSub(place);

        JPQLQuery<Long> reviewCount = createReviewCountSub(place);

        JPQLQuery<Long> bookmarkCount = createBookmarkCountSub(place);

        BooleanExpression isBookmarkChecked = isBookmarkCheckedSub(place, userId);

        List<PlaceReservedNotReviewedDto> notReviewListDtos = queryFactory.select(
                        Projections.constructor(PlaceReservedNotReviewedDto.class,
                                place.id,
                                placeReservation.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                reviewAvg,
                                reviewCount,
                                bookmarkCount,
                                isBookmarkChecked,
                                placeReservation.visitors,
                                placeReservation.checkIn,
                                placeReservation.checkOut,
                                placeReservation.content,
                                placeReservation.reservationStatus
                        )
                )
                .from(placeReservation)
                .join(placeReservation.place, place)
                .leftJoin(placeReservation.placeReview, placeReview)
                .where(
                        placeReservation.user.id.eq(userId),
                        place.placeStatus.eq(PostStatus.APPROVED),
                        placeReservation.reservationStatus.eq(PlaceReservationStatus.COMPLETED),
                        placeReservation.checkOut.lt(LocalDateTime.now()),
                        placeReservation.notReviewing.eq(false),
                        placeReview.isNull()
                )
                .orderBy(placeReservation.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> placeIdList = notReviewListDtos.stream().map(PlaceReservedNotReviewedDto::getId).toList();

//        가져온 id리스트를 in절의 조건으로 사진정보들을 가져온다.
        List<PlaceFileDto> fileDtoList = queryFactory.select(
                        Projections.constructor(PlaceFileDto.class,
                                placeFile.id,
                                placeFile.fileName,
                                placeFile.uuid,
                                placeFile.uploadPath,
                                placeFile.place.id
                        ))
                .from(placeFile)
                .where(placeFile.place.id.in(placeIdList))
                .orderBy(placeFile.id.asc(), placeFile.place.id.desc())
                .fetch();

        Map<Long, List<PlaceFileDto>> fileListMap = fileDtoList.stream().collect(Collectors.groupingBy(PlaceFileDto::getPlaceId));

//        장소 id별로 구분된 사진들을 각각 게시글 번호에 맞게 추가한다
        notReviewListDtos.forEach(placeListDto -> {
            placeListDto.updatePlaceFiles(fileListMap.get(placeListDto.getId())
                    .stream().limit(5L).toList());
            // 화면에서 뿌릴 주소값 가공
            placeListDto.getPlaceAddress().cutAddress();
        });

        return PageableExecutionUtils.getPage(notReviewListDtos, pageable, totalCountQuery::fetchOne);
    }

    private JPQLQuery<Double> createReviewAvgSub(QPlace place) {
        return JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.placeReservation.place.eq(place));
    }

    private JPQLQuery<Long> createReviewCountSub(QPlace place) {
        return JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.placeReservation.place.eq(place));
    }

    private JPQLQuery<Long> createBookmarkCountSub(QPlace place) {
        return JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));
    }

    private JPQLQuery<Long> createInquiryCountSub(QPlace place) {
        return JPAExpressions.select(placeInquiry.id.count())
                .from(placeInquiry)
                .where(placeInquiry.place.eq(place));
    }

    private BooleanExpression isBookmarkCheckedSub(QPlace place, Long userId) {
        return userId == null ?
                Expressions.asBoolean(false)
                : JPAExpressions.select(placeBookmark.id.isNotNull())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place).and(placeBookmark.user.id.eq(userId)))
                .exists();
    }


}






















