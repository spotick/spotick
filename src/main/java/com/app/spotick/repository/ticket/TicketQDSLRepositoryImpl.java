package com.app.spotick.repository.ticket;

import com.app.spotick.domain.dto.page.TicketPage;
import com.app.spotick.domain.dto.ticket.TicketFileDto;
import com.app.spotick.domain.dto.ticket.TicketGradeDto;
import com.app.spotick.domain.dto.ticket.TicketInfoDto;
import com.app.spotick.domain.dto.ticket.TicketManageListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.ticket.TicketRequestType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.spotick.domain.entity.ticket.QTicket.*;
import static com.app.spotick.domain.entity.ticket.QTicketFile.*;
import static com.app.spotick.domain.entity.ticket.QTicketGrade.*;
import static com.app.spotick.domain.entity.ticket.QTicketInquiry.*;

@RequiredArgsConstructor
public class TicketQDSLRepositoryImpl implements TicketQDSLRepository {
    private final JPAQueryFactory queryFactory;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TicketPage findHostTicketListByUserId(Long userId, Pageable pageable, TicketRequestType ticketRequestType) {

        JPAQuery<Long> totalCount = queryFactory.select(ticket.count())
                .from(ticket)
                .where(
                        ticket.user.id.eq(userId),
                        ticket.ticketEventStatus.ne(PostStatus.REPLACED),
                        ticket.ticketEventStatus.ne(PostStatus.DELETED)
                );

        JPAQuery<Long> upcomingCount = queryFactory.select(ticket.count())
                .from(ticket)
                .where(
                        ticket.user.id.eq(userId),
                        ticket.ticketEventStatus.ne(PostStatus.REPLACED),
                        ticket.ticketEventStatus.ne(PostStatus.DELETED),
                        ticket.endDate.after(LocalDate.now())
                );

        JPAQuery<Long> pastCount = queryFactory.select(ticket.count())
                .from(ticket)
                .where(
                        ticket.user.id.eq(userId),
                        ticket.ticketEventStatus.ne(PostStatus.REPLACED),
                        ticket.ticketEventStatus.ne(PostStatus.DELETED),
                        ticket.endDate.before(LocalDate.now())
                );

        JPQLQuery<Integer> minPrice = JPAExpressions.select(ticketGrade.price.min())
                .from(ticketGrade)
                .where(ticketGrade.ticket.eq(ticket))
                .groupBy(ticketGrade.ticket.id);

        JPQLQuery<Long> inquiriesCount = JPAExpressions.select(ticketInquiry.count())
                .from(ticketInquiry)
                .where(
                        ticketInquiry.ticket.eq(ticket),
                        ticketInquiry.response.isNull()
                );

        BooleanBuilder condition = new BooleanBuilder();

        switch (ticketRequestType) {
            case upcoming:
                condition.and(ticket.endDate.after(LocalDate.now()));
                break;
            case past:
                condition.and(ticket.endDate.before(LocalDate.now()));
                break;
        }

        List<TicketManageListDto> contents = queryFactory
                .select(Projections.constructor(TicketManageListDto.class,
                        ticket.id,
                        ticket.title,
                        ticket.ticketEventAddress,
                        ticket.ticketCategory,
                        minPrice,
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
                        condition,
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

        TicketPage result = new TicketPage();

        switch (ticketRequestType) {
            case all:
                result.setPage(PageableExecutionUtils.getPage(contents, pageable, totalCount::fetchOne));
                break;
            case upcoming:
                result.setPage(PageableExecutionUtils.getPage(contents, pageable, upcomingCount::fetchOne));
                break;
            case past:
                result.setPage(PageableExecutionUtils.getPage(contents, pageable, pastCount::fetchOne));
                break;
        }

        result.setTotal(totalCount.fetchOne());
        result.setUpcomingCount(upcomingCount.fetchOne());
        result.setPastCount(pastCount.fetchOne());

        return result;
    }

    @Override
    public Optional<TicketInfoDto> findTicketInfoByTicketId(Long ticketId, Long userId) {

        TicketInfoDto content = queryFactory
                .select(Projections.constructor(TicketInfoDto.class,
                        ticket.id,
                        ticket.title,
                        ticket.ticketEventAddress,
                        ticket.ticketCategory,
                        ticket.startDate,
                        ticket.endDate
                ))
                .from(ticket)
                .where(
                        ticket.id.eq(ticketId),
                        ticket.user.id.eq(userId)
                )
                .fetchOne();

        List<TicketGradeDto> ticketGrades = queryFactory
                .select(Projections.constructor(TicketGradeDto.class,
                        ticketGrade.gradeName,
                        ticketGrade.price,
                        ticketGrade.maxPeople,
                        ticketGrade.ticket.id
                ))
                .from(ticketGrade)
                .where(ticketGrade.ticket.id.eq(ticketId))
                .orderBy(ticketGrade.id.asc(), ticketGrade.ticket.id.desc())
                .fetch();

        Objects.requireNonNull(content).setTicketGradeDtos(ticketGrades);

        return Optional.of(content);
    }
}
