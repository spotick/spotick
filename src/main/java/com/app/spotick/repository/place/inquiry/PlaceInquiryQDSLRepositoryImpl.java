package com.app.spotick.repository.place.inquiry;

import com.app.spotick.domain.entity.place.PlaceInquiry;
import com.app.spotick.domain.entity.place.QPlaceInquiry;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.app.spotick.domain.entity.place.QPlaceInquiry.placeInquiry;

@RequiredArgsConstructor
public class PlaceInquiryQDSLRepositoryImpl implements PlaceInquiryQDSLRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<PlaceInquiry> inquiryListWithPage(Long placeId, Pageable pageable) {

        List<PlaceInquiry> contents = queryFactory.selectFrom(placeInquiry)
                .join(placeInquiry.user).fetchJoin()
                .where(placeInquiry.place.id.eq(placeId))
                .orderBy(placeInquiry.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(placeInquiry.count())
                .from(placeInquiry)
                .where(placeInquiry.place.id.eq(placeId))
                .fetchOne();

        return new PageImpl<>(contents,pageable,totalCount);
    }
}





















