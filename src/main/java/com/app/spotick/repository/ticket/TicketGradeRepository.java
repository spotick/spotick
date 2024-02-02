package com.app.spotick.repository.ticket;

import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketGradeRepository extends JpaRepository<TicketGrade,Long> {

}
