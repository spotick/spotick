package com.app.spotick.repository.ticket.order;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketOrder;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketOrderRepository extends JpaRepository<TicketOrder, Long>, TicketOrderQDSLRepository {
    @Query("""
                SELECT o.amount FROM TicketOrder o
                WHERE o.id = :orderId
            """)
    Optional<Long> findAmountById(@Param("orderId") Long orderId);

    @Modifying
    @Query("UPDATE TicketOrder to SET to.ticket = :changedTicket WHERE to.ticket = :originalTicket")
    void bulkUpdateOrderTicket(@Param("originalTicket") Ticket originalTicket, @Param("changedTicket")Ticket changedTicket);
}
