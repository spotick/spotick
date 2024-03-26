package com.app.spotick.repository.admin.ticket;

import com.app.spotick.domain.entity.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AdminTicketRepository extends JpaRepository<Ticket,Long>, AdminTicketQDSLRepository {
    //    티켓 수정 승인시 원본의 생성날짜를 바뀔 티켓에 적용하기 위해 직접 쿼리를 생성하여 적용시킨다.
    @Modifying
    @Query("UPDATE Ticket t SET t.createdDate = :originalCreatedDate WHERE t.id = :changedTicketId")
    void updateCreatedDateWithOriginal(@Param("originalCreatedDate") LocalDateTime originalCreatedDate, @Param("changedTicketId") Long changedTicketId);
}
