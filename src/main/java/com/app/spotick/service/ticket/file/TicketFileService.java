package com.app.spotick.service.ticket.file;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TicketFileService {

    TicketFile registerAndSaveTicketFile(MultipartFile ticketFile) throws IOException;
}
