package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.app.spotick.domain.entity.place.QPlace.*;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.*;
import static com.app.spotick.domain.entity.place.QPlaceFile.*;
import static com.app.spotick.domain.entity.place.QPlaceReview.*;

@RequiredArgsConstructor
public class PlaceBookmarkQDSLRepositoryImpl implements PlaceBookmarkQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceListDto> findBookmarkedPlacesByUserId(Long userId, Pageable pageable) {
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
            혹은 생성자에 모든 필드를 다 밀어 넣고 쿼리에서 null 값을 넣도록 설정하여도 무방.
         */
        List<PlaceListDto> placeListDtos = queryFactory.select(
                        Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                reviewAvg,
                                reviewCount,
                                bookmarkCount
                        )
                )
                .from(placeBookmark)
                .join(placeBookmark.place, place)
                .where(placeBookmark.user.id.eq(userId), place.placeStatus.eq(PostStatus.APPROVED))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

            /*
                화면DTO리스트 각각에 파일 리스트를 꽂아줘야 하므로 리스트을 가져온 뒤 그 id를 통해 파일을 전체 조회하여
                화면DTO에 파일리스트를 업데이트 시켜준다.
             */
        placeListDtos.forEach(placeListDto -> {
            // 평가결과가 null일 시 0.0으로 교체
            placeListDto.updateEvalAvg(Optional.ofNullable(placeListDto.getEvalAvg()).orElse(0.0));

            // 화면에서 뿌릴 주소값 가공
            placeListDto.getPlaceAddress().cutAddress();

            // 파일 결과값 찾기
            List<PlaceFileDto> placeFileDtos = queryFactory.select(
                            Projections.constructor(PlaceFileDto.class,
                                    placeFile.id,
                                    placeFile.fileName,
                                    placeFile.uuid,
                                    placeFile.uploadPath
                            )
                    )
                    .from(placeFile)
                    .where(placeFile.place.id.eq(placeListDto.getId()))
                    .fetch();

            // 화면Dto의 파일리스트에 결과값 저장
            placeListDto.updatePlaceFiles(placeFileDtos);
        });

        return placeListDtos;
    }
}


