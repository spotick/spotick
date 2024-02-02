package com.app.spotick.service.ticket.file;

import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.ticket.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TicketFileService {

    void registerAndSaveTicketFile(MultipartFile ticketFile, Ticket ticket) throws IOException;
}
