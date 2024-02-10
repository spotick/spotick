package com.app.spotick.repository.ticket;

import com.app.spotick.domain.dto.ticket.TicketFileDto;
import com.app.spotick.domain.dto.ticket.TicketGradeDto;
import com.app.spotick.domain.dto.ticket.TicketManageListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.app.spotick.domain.entity.ticket.QTicket.*;
import static com.app.spotick.domain.entity.ticket.QTicketFile.*;
import static com.app.spotick.domain.entity.ticket.QTicketGrade.*;
import static com.app.spotick.domain.entity.ticket.QTicketInquiry.*;

@RequiredArgsConstructor
public class TicketQDSLRepositoryImpl implements TicketQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TicketManageListDto> findHostTicketListByUserId(Long userId, Pageable pageable) {

        JPAQuery<Long> totalCount = queryFactory.select(ticket.count())
                .from(ticket)
                .where(
                        ticket.user.id.eq(userId),
                        ticket.ticketEventStatus.ne(PostStatus.REPLACED),
                        ticket.ticketEventStatus.ne(PostStatus.DELETED)
                );

        JPQLQuery<Long> inquiriesCount = JPAExpressions.select(ticketInquiry.count())
                .from(ticketInquiry)
                .where(
                        ticketInquiry.ticket.eq(ticket),
                        ticketInquiry.response.isNull()
                );

        List<TicketManageListDto> contents = queryFactory
                .select(Projections.constructor(TicketManageListDto.class,
                        ticket.id,
                        ticket.title,
                        ticket.ticketEventAddress,
                        ticket.ticketCategory,
                        ticket.startDate,
                        ticket.endDate,
                        Projections.constructor(TicketFileDto.class,
                                ticketFile.id,
                                ticketFile.fileName,
                                ticketFile.uuid,
                                ticketFile.uploadPath,
                                ticket.id
                        ),
                        inquiriesCount
                ))
                .from(ticket)
                .join(ticket.ticketFiles, ticketFile)
                .where(
                        ticket.user.id.eq(userId),
                        ticket.ticketEventStatus.ne(PostStatus.REPLACED),
                        ticket.ticketEventStatus.ne(PostStatus.DELETED),
                        ticketFile.id.eq(
                                JPAExpressions.select(ticketFile.id.min())
                                        .from(ticketFile)
                                        .where(ticketFile.ticket.id.eq(ticket.id))
                        )
                )
                .orderBy(ticket.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> ticketIdList = contents.stream().map(TicketManageListDto::getTicketId).toList();

        List<TicketGradeDto> ticketGrades = queryFactory.select(
                        Projections.constructor(TicketGradeDto.class,
                                ticketGrade.gradeName,
                                ticketGrade.price,
                                ticketGrade.maxPeople,
                                ticketGrade.ticket.id
                        )
                )
                .from(ticketGrade)
                .where(ticketGrade.ticket.id.in(ticketIdList))
                .orderBy(ticketGrade.id.asc(), ticketGrade.ticket.id.desc())
                .fetch();

        Map<Long, List<TicketGradeDto>> gradesMap = ticketGrades.stream().collect(Collectors.groupingBy(TicketGradeDto::getTicketId));

        contents.forEach(ticket -> ticket.setTicketGrades(gradesMap.get(ticket.getTicketId())));

        return PageableExecutionUtils.getPage(contents, pageable, totalCount::fetchOne);
    }
}
