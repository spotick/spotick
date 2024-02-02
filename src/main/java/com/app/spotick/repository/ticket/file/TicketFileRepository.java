package com.app.spotick.repository.ticket.file;

import com.app.spotick.domain.entity.ticket.TicketFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketFileRepository extends JpaRepository<TicketFile,Long> {

}
