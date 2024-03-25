package com.app.spotick.repository.ticket.order;

import com.app.spotick.domain.entity.ticket.TicketOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketOrderRepository extends JpaRepository<TicketOrder, Long> {
}
