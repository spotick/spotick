package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceDetailDto;
import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.entity.place.QPlace;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.placeFile;
import static com.app.spotick.domain.entity.place.QPlaceInquiry.placeInquiry;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;

@RequiredArgsConstructor
public class PlaceQDSLRepositoryImpl implements PlaceQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceListDto> findPlaceListPaging(Pageable pageable, Long userId) {
        List<PlaceListDto> placeListDtos = queryFactory.select(
                        Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                createReviewAvgSub(place),
                                createReviewCountSub(place),
                                createBookmarkCountSub(place),
                                isBookmarkCheckedSub(place, userId)
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

        placeListDtos.forEach(placeListDto -> {
//        장소 id별로 구분된 사진들을 각각 게시글 번호에 맞게 추가한다
            placeListDto.updatePlaceFiles(fileListMap.get(placeListDto.getId())
                    .stream().limit(5L).toList());

            // 평가결과가 null일 시 0.0으로 교체
            placeListDto.updateEvalAvg(Optional.ofNullable(placeListDto.getEvalAvg()).orElse(0.0));

            // 화면에서 뿌릴 주소값 가공
            placeListDto.getPlaceAddress().cutAddress();
        });
        return placeListDtos;
    }

    @Override
    public Optional<PlaceDetailDto> findPlaceDetailById(Long placeId, Long userId) {
        PlaceDetailDto placeDetailDto = queryFactory.select(
                        Projections.constructor(PlaceDetailDto.class,
                                place.id,
                                place.title,
                                place.subTitle,
                                place.info,
                                place.rule,
                                place.defaultPeople,
                                place.placeAddress.address,
                                place.placeAddress.addressDetail,
                                place.price,
                                place.surcharge,
                                place.user.id,
                                place.lat,
                                place.lng,
                                createReviewAvgSub(place),
                                createReviewCountSub(place),
                                createInquiryCountSub(place),
                                isBookmarkCheckedSub(place, userId)
                        )
                ).from(place)
                .where(place.id.eq(placeId))
                .fetchOne();

        placeDetailDto.setEvalAvg(Optional.ofNullable(placeDetailDto.getEvalAvg()).orElse(0.0));

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






















