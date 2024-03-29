package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.dto.ticket.grade.TicketGradeDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.ticket.TicketCategory;
import com.app.spotick.domain.type.ticket.TicketRatingType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TicketDetailDto {
    private Long ticketId;
    private Long hostId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private double lat;
    private double lng;
    private TicketCategory ticketCategory;
    private PostAddress ticketEventAddress;
    private TicketRatingType ticketRatingType;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private Long likeCount;
    private long inquiryCount;
    private boolean isLiked;

    private List<TicketGradeDto> ticketGradeDtos = new ArrayList<>();

    public TicketDetailDto(Long ticketId, Long hostId, String title, String content, LocalDate startDate, LocalDate endDate, double lat, double lng, TicketCategory ticketCategory, PostAddress ticketEventAddress, TicketRatingType ticketRatingType, String fileName, String uuid, String uploadPath, Long likeCount, long inquiryCount, boolean isLiked, List<TicketGradeDto> ticketGradeDtos) {
        this.ticketId = ticketId;
        this.hostId = hostId;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lat = lat;
        this.lng = lng;
        this.ticketCategory = ticketCategory;
        this.ticketEventAddress = ticketEventAddress;
        this.ticketRatingType = ticketRatingType;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.likeCount = likeCount;
        this.inquiryCount = inquiryCount;
        this.isLiked = isLiked;
        this.ticketGradeDtos = ticketGradeDtos;
    }
}
