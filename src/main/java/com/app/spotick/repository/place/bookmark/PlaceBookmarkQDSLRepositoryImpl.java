package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
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

import static com.app.spotick.domain.entity.place.QPlace.*;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.*;
import static com.app.spotick.domain.entity.place.QPlaceFile.*;
import static com.app.spotick.domain.entity.place.QPlaceReview.*;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class PlaceBookmarkQDSLRepositoryImpl implements PlaceBookmarkQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PlaceListDto> findBookmarkedPlacesByUserId(Long userId, Pageable pageable) {
        // 게시글 전체 갯수 확인
        JPAQuery<Long> totalCountQuery = queryFactory.select(place.count())
                .from(placeBookmark)
                .join(placeBookmark.place, place)
                .where(placeBookmark.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED));

        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        /*
            리스트 생성 쿼리, (생성자에서 placeFileDto 리스트는 제외하여 Projections.constructor 사용에 걸림이 없게 한다.
         */
//        List<PlaceListDto> placeListDtos = queryFactory.select(
//                        Projections.constructor(PlaceListDto.class,
//                                place.id,
//                                place.title,
//                                place.price,
//                                place.placeAddress,
//                                reviewAvg,
//                                reviewCount,
//                                bookmarkCount,
//                                // 북마크 리스트에서 북마크 체크는 필요없음. 이미되어있기때문에 가져와지기 때문.
//                                Expressions.constant(true)
//                        )
//                )
//                .from(placeBookmark)
//                .join(placeBookmark.place, place)
//                .where(placeBookmark.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED))
//                .orderBy(place.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        List<Long> placeIdList = placeListDtos.stream().map(PlaceListDto::getId).toList();
//
//        List<PlaceFileDto> fileDtoList = queryFactory.select(
//                        Projections.constructor(PlaceFileDto.class,
//                                placeFile.id,
//                                placeFile.fileName,
//                                placeFile.uuid,
//                                placeFile.uploadPath,
//                                placeFile.place.id
//                        ))
//                .from(placeFile)
//                .where(placeFile.place.id.in(placeIdList))
//                .orderBy(placeFile.id.asc(),placeFile.place.id.desc())
//                .fetch();
//
////        사진정보를 장소 id별로 묶는다
//        Map<Long, List<PlaceFileDto>> fileListMap = fileDtoList.stream().collect(Collectors.groupingBy(PlaceFileDto::getPlaceId));
//
////        장소 id별로 구분된 사진들을 각각 게시글 번호에 맞게 추가한다
//        placeListDtos.forEach(placeListDto->{
//            placeListDto.updatePlaceFiles(fileListMap.get(placeListDto.getId())
//                    .stream().limit(5L).toList());
//            // 평가결과가 null일 시 0.0으로 교체
//            placeListDto.updateEvalAvg(Optional.ofNullable(placeListDto.getEvalAvg()).orElse(0.0));
//            // 화면에서 뿌릴 주소값 가공
//            placeListDto.getPlaceAddress().cutAddress();
//        });

//트랜스폼 방안
        List<PlaceListDto> placeListDtos = queryFactory.select(place)
                .from(placeBookmark)
                .join(placeBookmark.place, place)
                .innerJoin(placeFile)
                .on(place.id.eq(placeFile.place.id))
                .where(placeBookmark.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED))
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
                                // 북마크 리스트에서 북마크 체크는 필요없음. 이미되어있기때문에 가져와지기 때문.
                                Expressions.constant(true)
                        ))
                );

        return PageableExecutionUtils.getPage(placeListDtos, pageable, totalCountQuery::fetchOne);
    }
}


