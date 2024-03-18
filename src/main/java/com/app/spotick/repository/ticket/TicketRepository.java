package com.app.spotick.repository.ticket;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketInquiry;
import com.app.spotick.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long>, TicketQDSLRepository {
    Optional<Ticket> findByIdAndUser(Long id, User user);
}
