package com.app.spotick.repository.ticket.order;

import com.app.spotick.domain.entity.ticket.TicketOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketOrderDetailRepository extends JpaRepository<TicketOrderDetail, Long> {
}
