package com.app.spotick.repository.ticket.modifyRequest;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceModifyRequest;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketModifyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketModifyReqRepository extends JpaRepository<TicketModifyRequest,Long> {

    @Query("""
        SELECT mr FROM TicketModifyRequest mr
        JOIN FETCH mr.changedTicket
        JOIN FETCH mr.originalTicket
        WHERE mr.changedTicket =:changedTicket
        """)
    Optional<TicketModifyRequest> findByChangedTicket(@Param("changedTicket") Ticket changedTicket);
}
