package com.app.spotick.domain.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TicketFileDto {
    private Long ticketFileId;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private Long ticketId;

    public TicketFileDto(Long ticketFileId, String fileName, String uuid, String uploadPath, Long ticketId) {
        this.ticketFileId = ticketFileId;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.ticketId = ticketId;
    }
}
