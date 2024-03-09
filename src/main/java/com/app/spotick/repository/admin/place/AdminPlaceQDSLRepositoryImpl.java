package com.app.spotick.repository.admin.place;

import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
import com.app.spotick.domain.entity.place.QPlace;
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
    public Slice<AdminPlaceListDto> findAdminPlaceList(Pageable pageable) {

        List<AdminPlaceListDto> adminPlaceList = queryFactory.select(
                        Projections.constructor(AdminPlaceListDto.class,
                                place.id,
                                place.user.email,
                                place.title,
                                place.createdDate,
                                place.placeStatus
                        )
                )
                .from(place)
                .join(place.user)
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
}

















