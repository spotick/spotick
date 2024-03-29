package com.app.spotick.repository.admin.place;

import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.app.spotick.domain.entity.place.QPlace.place;

@RequiredArgsConstructor
public class AdminPlaceQDSLRepositoryImpl implements AdminPlaceQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<AdminPostListDto> findAdminPlaceList(Pageable pageable, AdminPostSearchDto placeSearchDto) {

        List<AdminPostListDto> adminPlaceList = queryFactory.select(
                        Projections.constructor(AdminPostListDto.class,
                                place.id,
                                place.user.email,
                                place.title,
                                place.createdDate,
                                place.placeStatus
                        )
                )
                .from(place)
                .join(place.user)
                .where(createSearchCondition(placeSearchDto))
                .orderBy(place.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (adminPlaceList.size() > pageable.getPageSize()) {
            adminPlaceList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(adminPlaceList,pageable,hasNext);
    }

    private BooleanBuilder createSearchCondition(AdminPostSearchDto placeSearchDto){
        BooleanBuilder builder = new BooleanBuilder();
        if (placeSearchDto.getEmail() != null) {
            builder.and(place.user.email.contains(placeSearchDto.getEmail()));
        }
        if (placeSearchDto.getPostTitle() != null) {
            builder.and(place.title.contains(placeSearchDto.getPostTitle()));
        }
        if (placeSearchDto.getStatus() != null) {
            builder.and(place.placeStatus.eq(placeSearchDto.getStatus()));
        }
        return builder;
    }
}

















