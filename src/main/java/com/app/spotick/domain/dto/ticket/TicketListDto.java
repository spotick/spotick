package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TicketListDto {
    private Long ticketId;
    private String ticketTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private TicketCategory ticketCategory;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private Long likeCount;
    private Integer lowestPrice;
    private String postAddress;
    private boolean isLiked;

    public TicketListDto(Long ticketId, String ticketTitle, LocalDate startDate, LocalDate endDate, TicketCategory ticketCategory, String fileName, String uuid, String uploadPath, Long likeCount, Integer lowestPrice, String postAddress, boolean isLiked) {
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketCategory = ticketCategory;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.likeCount = likeCount;
        this.lowestPrice = lowestPrice;
        this.postAddress = postAddress;
        this.isLiked = isLiked;
    }

    // 리스트 화면에서 뿌리기 위해 사용할 주소 가공 함수
    // 서울특별시 강남구 테헤란로 202 => 서울특별시 강남구
    public void cutAddress(){
        int cutPoint = postAddress.indexOf(" ", postAddress.indexOf(" ") + 1);
        postAddress = postAddress.substring(0, cutPoint);
    }
}
