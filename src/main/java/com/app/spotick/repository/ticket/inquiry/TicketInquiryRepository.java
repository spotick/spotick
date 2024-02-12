package com.app.spotick.repository.ticket.inquiry;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketInquiryRepository extends JpaRepository<TicketInquiry, Long>, TicketInquiryQDSLRepository {

    Optional<TicketInquiry> findByIdAndTicket(Long id, Ticket ticket);
}
