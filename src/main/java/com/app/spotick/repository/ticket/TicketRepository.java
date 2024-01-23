package com.app.spotick.repository.ticket;

import com.app.spotick.domain.entity.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
//    티켓 등록 (기본 save() 사용)
//    티켓 상세보기
//    티켓 리스트(페이징)
//    티켓 수정
//    티켓 삭제
}
