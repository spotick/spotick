package com.app.spotick.repository.ticket.inquiry;

import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.entity.ticket.QTicket;
import com.app.spotick.domain.entity.ticket.QTicketInquiry;
import com.app.spotick.domain.entity.user.QUser;
import com.app.spotick.domain.entity.user.QUserProfileFile;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.app.spotick.domain.entity.ticket.QTicket.*;
import static com.app.spotick.domain.entity.ticket.QTicketInquiry.*;
import static com.app.spotick.domain.entity.user.QUser.*;
import static com.app.spotick.domain.entity.user.QUserProfileFile.*;

@RequiredArgsConstructor
public class TicketInquiryQDSLRepositoryImpl implements TicketInquiryQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<UnansweredInquiryDto> findUnansweredInquiriesSlice(Long ticketId, Long userId, Pageable pageable) {

        List<UnansweredInquiryDto> contents = queryFactory
                .from(ticketInquiry)
                .join(ticketInquiry.user, user)
                .join(user.userProfileFile, userProfileFile)
                .where(
                        ticketInquiry.ticket.id.eq(ticketId),
                        ticket.user.id.eq(userId),
                        ticketInquiry.response.isNull()
                )
                .orderBy(ticketInquiry.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .select(
                        Projections.constructor(UnansweredInquiryDto.class,
                                ticketInquiry.id,
                                ticketInquiry.title,
                                ticketInquiry.content,
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
