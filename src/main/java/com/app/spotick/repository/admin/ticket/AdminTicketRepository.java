package com.app.spotick.repository.admin.ticket;

import com.app.spotick.domain.entity.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTicketRepository extends JpaRepository<Ticket,Long>, AdminTicketQDSLRepository {

}
