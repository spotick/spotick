package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceDetailDto;
import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.QPlace;
import com.app.spotick.domain.entity.place.QPlaceReview;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.Tuple;
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
import static com.app.spotick.domain.entity.place.QPlaceInquiry.placeInquiry;
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

        //        로그인 되어있지 않으면 쿼리 실행 x
        BooleanExpression isBookmarkChecked = isBookmarkCheckedSub(place, userId);

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
            dto.cutPlaceFilesForListPage();
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
                .where(subReview.place.id.eq(placeId), subReview.score.eq(5));


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

    private JPQLQuery<Double> createReviewAvgSub(QPlace place) {
        return JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));
    }

    private JPQLQuery<Long> createReviewCountSub(QPlace place) {
        return JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));
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






















