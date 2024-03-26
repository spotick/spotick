package com.app.spotick.repository.ticket.order;

import com.app.spotick.domain.entity.ticket.TicketOrderDetail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketOrderDetailRepository extends JpaRepository<TicketOrderDetail, Long> {

    @Modifying
    @Query("delete from TicketOrderDetail tod where tod.ticketOrder.id = :ticketOrderId")
    void deleteAllByTicketOrderId(@Param("ticketOrderId") Long ticketOrderId);

}
