package com.app.spotick.repository.ticket.grade;

import com.app.spotick.domain.dto.ticket.TicketGradeSaleInfoDto;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketGradeRepository extends JpaRepository<TicketGrade, Long> {

    @Query("""
            select new com.app.spotick.domain.dto.ticket.TicketGradeSaleInfoDto(
                tg.gradeName,
                tg.price,
                COALESCE((SELECT SUM(td.quantity) FROM TicketOrderDetail td WHERE td.ticketGrade = tg and td.ticketOrder.eventDate = :date), 0),
                tg.maxPeople
            )
            from TicketGrade tg
            where tg.ticket.id = :ticketId
            order by tg.price asc
            """)
    List<TicketGradeSaleInfoDto> findTicketGradesByTicketId(@Param("ticketId") Long ticketId, @Param("date") LocalDate date);

}
