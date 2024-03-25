package com.app.spotick.repository.ticket;

import com.app.spotick.domain.dto.page.TicketPage;
import com.app.spotick.domain.dto.ticket.*;
import com.app.spotick.domain.dto.ticket.grade.TicketGradeDto;
import com.app.spotick.domain.dto.ticket.grade.TicketGradeSaleInfoDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.ticket.TicketRequestType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.app.spotick.domain.entity.ticket.QTicket.*;
import static com.app.spotick.domain.entity.ticket.QTicketFile.*;
import static com.app.spotick.domain.entity.ticket.QTicketGrade.*;
import static com.app.spotick.domain.entity.ticket.QTicketInquiry.*;
import static com.app.spotick.domain.entity.ticket.QTicketLike.*;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class TicketQDSLRepositoryImpl implements TicketQDSLRepository {
    private final JPAQueryFactory queryFactory;

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
                        ticketFile.fileName,
                        ticketFile.uuid,
                        ticketFile.uploadPath,
                        inquiriesCount
                ))
                .from(ticket)
                .join(ticket.ticketFile, ticketFile)
                .where(
                        ticket.user.id.eq(userId),
                        ticket.ticketEventStatus.ne(PostStatus.REPLACED),
                        ticket.ticketEventStatus.ne(PostStatus.DELETED),
                        condition
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

        List<TicketGradeSaleInfoDto> ticketGrades = queryFactory
                .select(Projections.constructor(TicketGradeSaleInfoDto.class,
                        ticketGrade.gradeName,
                        ticketGrade.price,
                        ticketGrade.id, //불필요 정보
                        ticketGrade.maxPeople
                ))
                .from(ticketGrade)
                .where(ticketGrade.ticket.id.eq(ticketId))
                .orderBy(ticketGrade.id.asc(), ticketGrade.ticket.id.desc())
                .fetch();

        Objects.requireNonNull(content).setTicketGradeSaleInfoDtos(ticketGrades);

        return Optional.of(content);
    }

    @Override
    public Slice<TicketListDto> findTicketListPage(Pageable pageable, Long userId) {

        JPQLQuery<Integer> lowestPrice = JPAExpressions.select(ticketGrade.price.min())
                .from(ticketGrade)
                .where(ticketGrade.ticket.eq(ticket))
                .groupBy(ticketGrade.ticket);

        List<TicketListDto> contents = queryFactory
                .from(ticket)
                .where(ticket.ticketEventStatus.eq(PostStatus.APPROVED))
                .orderBy(ticket.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .select(Projections.constructor(TicketListDto.class,
                        ticket.id,
                        ticket.title,
                        ticket.startDate,
                        ticket.endDate,
                        ticket.ticketCategory,
                        ticket.ticketFile.fileName,
                        ticket.ticketFile.uuid,
                        ticket.ticketFile.uploadPath,
                        likeCount(),
                        lowestPrice,
                        ticket.ticketEventAddress.address,
                        isLiked(userId)
                ))
                .fetch();

        contents.forEach(TicketListDto::cutAddress);

        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Optional<TicketDetailDto> findTicketDetailById(Long ticketId, Long userId) {

        // todo: startDate ~ endDate 사이의 각 등급들과 그 등급들의 판매갯수가 몇개인지 가져올 수 있어야 하며 가능하다면 하나의 쿼리로 해결해야함.

        List<TicketDetailDto> content = queryFactory
                .from(ticket)
                .where(
                        ticket.id.eq(ticketId),
                        ticket.ticketEventStatus.eq(PostStatus.APPROVED)
                )
                .leftJoin(ticket.ticketGrades, ticketGrade)
                .orderBy(ticketGrade.price.asc())
                .transform(groupBy(ticket.id).list(Projections.constructor(TicketDetailDto.class,
                                ticket.id,
                                ticket.user.id,
                                ticket.title,
                                ticket.content,
                                ticket.startDate,
                                ticket.endDate,
                                ticket.lat,
                                ticket.lng,
                                ticket.ticketCategory,
                                ticket.ticketEventAddress,
                                ticket.ticketRatingType,
                                ticket.ticketFile.fileName,
                                ticket.ticketFile.uuid,
                                ticket.ticketFile.uploadPath,
                                likeCount(),
                                inquiryCount(),
                                isLiked(userId),
                                list(Projections.constructor(TicketGradeDto.class,
                                        ticketGrade.gradeName,
                                        ticketGrade.price,
                                        ticketGrade.maxPeople
                                ))
                        )
                ));

        return Optional.ofNullable(content.get(0));
    }

    @Override
    public Optional<TicketEditDto> findTicketEditById(Long ticketId, Long userId) {

        List<TicketEditDto> content = queryFactory
                .from(ticket)
                .where(ticket.id.eq(ticketId), ticket.user.id.eq(userId))
                .leftJoin(ticket.ticketGrades, ticketGrade)
                .orderBy(ticketGrade.price.asc())
                .transform(groupBy(ticket.id).list(Projections.constructor(TicketEditDto.class,
                                ticket.id,
                                ticket.user.id,
                                ticket.title,
                                ticket.content,
                                ticket.startDate,
                                ticket.endDate,
                                ticket.bankName,
                                ticket.accountNumber,
                                ticket.accountHolder,
                                ticket.ticketEventAddress.address,
                                ticket.ticketEventAddress.addressDetail,
                                ticket.ticketRatingType,
                                ticket.ticketCategory,
                                ticket.lat,
                                ticket.lng,
                                ticket.ticketFile.id,
                                ticket.ticketFile.fileName,
                                ticket.ticketFile.uuid,
                                ticket.ticketFile.uploadPath,
                                list(Projections.constructor(TicketGradeDto.class,
                                        ticketGrade.gradeName,
                                        ticketGrade.price,
                                        ticketGrade.maxPeople
                                ))
                        )
                ));

        return Optional.ofNullable(content.get(0));
    }

    private JPQLQuery<Long> likeCount() {
        return JPAExpressions.select(ticketLike.count())
                .from(ticketLike)
                .where(ticketLike.ticket.eq(ticket));
    }

    private JPQLQuery<Long> inquiryCount() {
        return JPAExpressions.select(ticketInquiry.count())
                .from(ticketInquiry)
                .where(ticketInquiry.ticket.eq(ticket));
    }

    private BooleanExpression isLiked(Long userId) {
        return userId == null ?
                Expressions.asBoolean(false)
                : JPAExpressions.select(ticketLike.id.isNotNull())
                .from(ticketLike)
                .where(ticketLike.ticket.eq(ticket).and(ticketLike.user.id.eq(userId)))
                .exists();
    }

}
