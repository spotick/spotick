package com.app.spotick.repository.ticket.order;

import com.app.spotick.api.dto.bootpay.BootpayItemDto;
import com.app.spotick.api.dto.ticket.TicketOrderDto;
import com.app.spotick.domain.entity.ticket.QTicket;
import com.app.spotick.domain.entity.ticket.QTicketOrder;
import com.app.spotick.domain.entity.ticket.QTicketOrderDetail;
import com.app.spotick.domain.entity.user.QUser;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.app.spotick.domain.entity.ticket.QTicket.*;
import static com.app.spotick.domain.entity.ticket.QTicketOrder.*;
import static com.app.spotick.domain.entity.ticket.QTicketOrderDetail.*;
import static com.app.spotick.domain.entity.user.QUser.*;
import static com.querydsl.core.group.GroupBy.*;

@RequiredArgsConstructor
public class TicketOrderQDSLRepositoryImpl implements TicketOrderQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public TicketOrderDto.Info findOrderInfoById(Long orderId) {

        List<TicketOrderDto.Info> content = queryFactory
                .from(ticketOrder)
                .where(ticketOrder.id.eq(orderId))
                .join(ticketOrder.ticket, ticket)
                .join(ticketOrder.user, user)
                .leftJoin(ticketOrder.ticketOrderDetails, ticketOrderDetail)
                .orderBy(ticketOrderDetail.id.asc())
                .transform(groupBy(ticketOrder.id).list(Projections.constructor(TicketOrderDto.Info.class,
                        ticketOrder.id,
                        ticketOrder.amount,
                        ticketOrder.paymentMethod,
                        ticket.title,
                        user.id,
                        user.nickName,
                        user.tel,
                        user.email,
                        list(Projections.constructor(BootpayItemDto.class,
                                ticketOrderDetail.ticketGrade.id,
                                ticketOrderDetail.ticketGrade.gradeName,
                                ticketOrderDetail.quantity,
                                ticketOrderDetail.ticketGrade.price
                        ))
                )));

        return content.get(0);
    }
}
