package com.app.spotick.repository.admin.ticket;

import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import com.app.spotick.domain.entity.place.QPlace;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.ticket.QTicket.ticket;

@RequiredArgsConstructor
public class AdminTicketQDSLRepositoryImpl implements AdminTicketQDSLRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Slice<AdminPostListDto> findAdminTicketList(Pageable pageable, AdminPostSearchDto ticketSearchDto) {

        List<AdminPostListDto> adminPlaceList = queryFactory.select(
                        Projections.constructor(AdminPostListDto.class,
                                ticket.id,
                                ticket.user.email,
                                ticket.title,
                                ticket.createdDate,
                                ticket.ticketEventStatus
                        )
                )
                .from(ticket)
                .join(ticket.user)
                .where(createSearchCondition(ticketSearchDto))
                .orderBy(ticket.id.desc())
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

    private BooleanBuilder createSearchCondition(AdminPostSearchDto ticketSearchDto){
        BooleanBuilder builder = new BooleanBuilder();
        if (ticketSearchDto.getEmail() != null) {
            builder.and(ticket.user.email.contains(ticketSearchDto.getEmail()));
        }
        if (ticketSearchDto.getPostTitle() != null) {
            builder.and(ticket.title.contains(ticketSearchDto.getPostTitle()));
        }
        if (ticketSearchDto.getStatus() != null) {
            builder.and(ticket.ticketEventStatus.eq(ticketSearchDto.getStatus()));
        }
        return builder;
    }
}
